/**********************************************
 * [Copyright ©]
 * @File: SetLibrary.java 
 * @author Frank-Wang
 * @Date: 2019.04.19
 * @Version: 1.0 
 * @Since: JDK 1.8.0_92, AGILE 9.3.6
 **********************************************/
package library;

import com.agile.api.APIException;
import com.agile.api.IAgileList;
import com.agile.api.ICell;
import com.agile.api.IDataObject;
import com.agile.api.IRow;
import com.agile.api.ItemConstants;

import com.anselm.tools.agile.AUtil;
import com.anselm.tools.record.Log;

/**
 * SetLibrary
 *
 */
public class SetLibrary {
	public static Boolean simpleSet(Log log, IDataObject iDataObject, Integer baseID, String strData) {
		Boolean flagCheck = false;

		try {
			// if (AUtil.IsNotNULL(iDataObject)) {
			iDataObject.setValue(baseID, strData);
			// }
		} catch (APIException e) {
			if (e.getErrorCode().equals(60018)) {
				log.log("Input " + strData + " into " + baseID + " failed.");
				flagCheck = true;
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagCheck;
	}

	public static Boolean listSet(Log log, IDataObject iDataObject, Integer baseID, IAgileList[] aryData) {
		Boolean flagCheck = false;

		try {
			// if (AUtil.IsNotNULL(iDataObject)) {
			ICell icell = iDataObject.getCell(baseID);

			IAgileList ialValues = icell.getAvailableValues();
			ialValues.setSelection(aryData);
			icell.setValue(ialValues);
			// }
		} catch (APIException e) {
			if (e.getErrorCode().equals(60018)) {
				log.log("Input list data into " + baseID + " failed.");
				flagCheck = true;
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagCheck;
	}

	public static Boolean simpleSetRedLine(Log log, IRow iRow, Integer baseID, String strData) {
		Boolean flagCheck = false;

		try {
			// if (AUtil.IsNotNULL(iRow)) {
			ICell iCell = iRow.getCell(baseID);
			iCell.setValue(strData);
			// }
		} catch (APIException e) {
			if (e.getErrorCode().equals(60018)) {
				log.log("Input " + strData + " into " + baseID + " failed.");
				flagCheck = true;
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagCheck;
	}

	public static Boolean listSetRedLine(Log log, IRow iRow, Integer baseID, IAgileList[] aryData) {
		Boolean flagCheck = false;

		try {
			// if (AUtil.IsNotNULL(iRow)) {
			ICell icell = iRow.getCell(baseID);

			IAgileList ialValues = icell.getAvailableValues();
			ialValues.setSelection(aryData);
			icell.setValue(ialValues);
			// }
		} catch (APIException e) {
			if (e.getErrorCode().equals(60018)) {
				log.log("Input list data into " + baseID + " failed.");
				flagCheck = true;
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagCheck;
	}
}