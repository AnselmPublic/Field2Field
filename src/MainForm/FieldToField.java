/**********************************************
 * [Copyright ©]
 * @File: FeildToField.java 
 * @author Frank-Wang
 * @Date: 2019.04.19
 * @Version: 1.0 
 * @Since: JDK 1.8.0_92, AGILE 9.3.6
 **********************************************/

package MainForm;

import java.awt.image.BandedSampleModel;
import java.util.Iterator;

import com.agile.api.APIException;
import com.agile.api.ChangeConstants;
import com.agile.api.DataTypeConstants;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.ICell;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ItemConstants;
import com.anselm.tools.agile.AUtil;
import com.anselm.tools.record.Ini;
import com.anselm.tools.record.Log;

import library.GetLibrary;
import library.SetLibrary;
import sun.util.logging.resources.logging;

/**
 * Class FieldToField is to bring the value of one field to another field.
 * <P/>
 * This code is focus on calling {@link library.GetLibrary} to get value and
 * calling {@link library.SetLibrary} to set value.
 * <P/>
 * 
 * All function below: <BR/>
 */
public class FieldToField {
	public Boolean judge2Field(IDataObject ido, String field1, String field2, Log log, Ini ini) {
		Boolean check = true;

		// Get admin session.
		log.log("▶ Get Admin session ..");
		IAgileSession admin = getAdminSession(ini);

		// Check if it will call same object
		if (field1.indexOf("@") == -1 && field2.indexOf("@") == -1) {
			check = doActionInSameObject(ido, field1, field2, log, admin);
		} else {
			check = doActionBetweenChangeAndItem(ido, field1, field2, log, admin);
		}

		return check;
	}

	/**
	 * doActionInSameObject is for two field in same object.
	 * <P/>
	 * Same object means two fields are all in the same Item, Change, etc..<BR/>
	 * It can choose to be in different page, but must both in cover page, for
	 * example like one field in page two another in page three.<BR/>
	 * The field type will judge in {@link library.GetLibrary} and
	 * {@link library.SetLibrary}.<BR/>
	 * 
	 * @param ido    &emsp;The object that will be setting in. It can be item,
	 *               change, etc..
	 * @param field1 Get values ​​from this field.<BR/>
	 *               &emsp;&emsp;&emsp;Please input some string like <em><b>"物件,欄位
	 *               Base ID"</b></em>.<BR/>
	 *               &emsp;&emsp;&emsp;For example like "Item,1521".
	 * @param field2 Same as field1.
	 * @param log    &emsp;For writing out messages.
	 * @param admin  It's the administrator session.
	 * 
	 * @return Return a string in error messages.
	 */
	private Boolean doActionInSameObject(IDataObject ido, String field1, String field2, Log log, IAgileSession admin) {
		Boolean flagCheck = false;

		String[] aryField1Details = separate(field1, true);
		String[] aryField2Details = separate(field2, true);

		try {
			ICell iCellGet = ido.getCell(Integer.parseInt(aryField1Details[1]));
			ICell iCellSet = ido.getCell(Integer.parseInt(aryField2Details[1]));

			if (iCellGet.getAttribute().getDataType() == iCellSet.getAttribute().getDataType()) {
				IAgileList[] ialDataOfList = null;
				String strDataOfString = "";

				// Get value
				if (iCellGet.getAttribute().getDataType() == DataTypeConstants.TYPE_SINGLELIST
						|| iCellGet.getAttribute().getDataType() == DataTypeConstants.TYPE_MULTILIST) {
					ialDataOfList = GetLibrary.listGet(ido, Integer.parseInt(aryField1Details[1]));
					log.logger("Get data [" + aryIAgileList2String(ialDataOfList) + "] in base id ["
							+ aryField1Details[1] + "]");
				} else {
					strDataOfString = GetLibrary.simpleGet(ido, Integer.parseInt(aryField1Details[1]));
					log.logger("Get data [" + strDataOfString + "] in base id [" + aryField1Details[1] + "]");
				}

				// Set value
				if (iCellSet.getAttribute().getDataType() == DataTypeConstants.TYPE_SINGLELIST
						|| iCellGet.getAttribute().getDataType() == DataTypeConstants.TYPE_MULTILIST) {
					log.logger("Input data [" + aryIAgileList2String(ialDataOfList) + "] to base id ["
							+ aryField2Details[1] + "]");
					flagCheck = SetLibrary.listSet(log, ido, Integer.parseInt(aryField2Details[1]), ialDataOfList);
				} else {
					log.logger("Input data [" + strDataOfString + "] to base id [" + aryField2Details[1] + "]");
					flagCheck = SetLibrary.simpleSet(log, ido, Integer.parseInt(aryField2Details[1]), strDataOfString);
				}
			} else {
				log.log("Two fields are different type.");
				flagCheck = true;
			}
		} catch (APIException e) {
			e.printStackTrace();
			log.logException(e);
		} catch (Exception e) {
			e.printStackTrace();
			log.logException(e);
		}

		return flagCheck;
	}

	public Boolean doActionBetweenChangeAndItem(IDataObject ido, String field1, String field2, Log log,
			IAgileSession admin) {
		Boolean flagCheck = false;
		String strError = "";
		String[] aryIdo1Details = null, aryIdo2Details = null;

		String[] aryField1Details = separate(field1, true);
		String[] aryField2Details = separate(field2, true);

		try {
			// Get value
			if (aryField1Details[0].indexOf("@") != -1) {
				aryIdo1Details = separate(aryField1Details[0], false);
			}
			if (aryIdo1Details != null) {
				if (aryIdo1Details[1].toLowerCase().contains("redline")) {
					flagCheck = getOrSetForRedLineTable(log, ido, aryIdo1Details[1], aryIdo1Details[2],
							Integer.parseInt(aryField1Details[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.logException(e);
		}

		return flagCheck;
	}

	/**
	 * Separate is for separate String <BR/>
	 * Separate string by "," or "@".<BR/>
	 * 
	 * @see String[] java.lang.String.split(String regex)
	 * 
	 * @param _string    &emsp;&emsp;Details of Field not been separate.
	 * @param flagChoose Choosing what to separate by true and false.
	 *                   <P/>
	 *                   <em>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;True : Separate
	 *                   by&nbsp; ",".<BR/>
	 *                   &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;False : Separate
	 *                   by&nbsp; "@".</em>
	 * 
	 * @return String[] : All field information.
	 */
	private String[] separate(String _string, Boolean flagChoose) {
		/* For return separate details */
		String[] aryDetails = null;

		if (flagChoose) {
			aryDetails = _string.trim().split(",");
		} else {
			aryDetails = _string.trim().split("@");
		}

		return aryDetails;
	}

	/**
	 * &emsp;&emsp;&emsp;&emsp;<em><b>getAdminSession</em></b><BR/>
	 * <BR/>
	 * &emsp;<font size=
	 * "1">{@linkplain IAgileSession}&emsp;getAdminSession({@linkplain Ini}
	 * ini)</font><BR/>
	 * <BR/>
	 * Obtain system administrator authority information by reading config. For
	 * detailed visits, please refer to {@link util.AUtil}.
	 * 
	 * @param ini All tools for config.For detailed visits, please refer to
	 *            {@link util.Ini}.
	 * 
	 * @return Administrator session.For detailed visits, please refer to
	 *         {@link com.agile.api.IAgileSession}.
	 */
	private IAgileSession getAdminSession(Ini ini) {
		String strAgileUrl = ini.getValue("AgileAP", "url");
		String strAgileAdmin = ini.getValue("AgileAP", "username");
		String strAgilepwd = ini.getValue("AgileAP", "password");
		IAgileSession admin = AUtil.getAgileSession(strAgileUrl, strAgileAdmin, strAgilepwd);

		return admin;
	}

	private Boolean getOrSetForRedLineTable(Log log, IDataObject ido, String strType, String strCriteria,
			Integer baseID) {
		Boolean flagCheck = false;
		try {
			switch (strCriteria) {
			case "0":
				IChange iChange = (IChange) ido;
				ITable iTableAffected = iChange.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);
				Iterator<?> it = iTableAffected.iterator();
				// loop through affected Items
				while (it.hasNext()) {
					// get affected item
					IRow iRow = (IRow) it.next();
					IItem iItem = (IItem) iRow.getReferent();
					ITable iTableRedLine = null;

					if (strType.toUpperCase().equals("REDLINETITLEBLOCK")) {
						iTableRedLine = iItem.getTable(ItemConstants.TABLE_REDLINETITLEBLOCK);
					} else if (strType.toUpperCase().equals("REDLINEPAGETWO")) {
						iTableRedLine = iItem.getTable(ItemConstants.TABLE_REDLINEPAGETWO);
					} else if (strType.toUpperCase().equals("REDLINEPAGETHREE")) {
						iTableRedLine = iItem.getTable(ItemConstants.TABLE_REDLINEPAGETHREE);
					} else if (strType.toUpperCase().equals("TABLE_REDLINEBOM")) {
						iTableRedLine = iItem.getTable(ItemConstants.TABLE_REDLINEBOM);
					} else if (strType.toUpperCase().equals("REDLINEMANUFACTURERS")) {
						iTableRedLine = iItem.getTable(ItemConstants.TABLE_REDLINEMANUFACTURERS);
					}

					if (iTableRedLine == null) {
						log.log("Error");
					} else {
						IRow iRowRedLine = (IRow) iTableRedLine.iterator().next();
						ICell iCell = iRowRedLine.getCell(baseID);

						IAgileList[] ialDataOfList = null;
						String strDataOfString = "";

						// Get value
						if (iCell.getAttribute().getDataType() == DataTypeConstants.TYPE_SINGLELIST
								|| iCell.getAttribute().getDataType() == DataTypeConstants.TYPE_MULTILIST) {
							ialDataOfList = GetLibrary.listGetRedLine(iRowRedLine, baseID);
						} else {
							strDataOfString = GetLibrary.simpleGetRedLine(iRowRedLine, baseID);
							System.out.println(strDataOfString);
						}

						// Set value
						if (iCell.getAttribute().getDataType() == DataTypeConstants.TYPE_SINGLELIST
								|| iCell.getAttribute().getDataType() == DataTypeConstants.TYPE_MULTILIST) {
							flagCheck = SetLibrary.listSetRedLine(log, iRowRedLine, baseID, ialDataOfList);
						} else {
							flagCheck = SetLibrary.simpleSetRedLine(log, iRowRedLine, baseID, strDataOfString);
						}

					}
				}
				break;
			}
		} catch (APIException e) {
			e.printStackTrace();
			log.logException(e);
		}

		return flagCheck;
	}

	private String aryIAgileList2String(IAgileList[] aryIAgileList) throws APIException {
		String data = "";

		for (int i = 0; i < aryIAgileList.length; i++) {
			data += (i == 0 ? "" : ",") + aryIAgileList[i].getValue();
		}

		return data;
	}
}
