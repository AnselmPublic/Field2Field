/**********************************************
 * [Copyright ©]
 * @File: GetLibrary.java 
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

/**
 * Class GetLibrary is to get the value of the field. <BR/>
 * It include getting different type of field.
 * <P/>
 * 
 * All function below :<BR/>
 * {@link #judgeType(IDataObject, String[])}
 */
public class GetLibrary {
	public static IAgileList[] listGet(IDataObject ido, Integer baseID) {
		IAgileList[] aryData = null;

		try {
			ICell icell = ido.getCell(baseID);
			IAgileList ial = (IAgileList) icell.getValue();
			aryData = ial.getSelection();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return aryData;
	}

	public static String simpleGet(IDataObject ido, Integer baseID) {
		String strData = null;

		try {
			strData = ido.getValue(baseID).toString();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strData;
	}

	public static IAgileList[] listGetRedLine(IRow iRow, Integer baseID) {
		IAgileList[] aryData = null;

		try {
			ICell icell = iRow.getCell(baseID);
			IAgileList ial = (IAgileList) icell.getValue();
			aryData = ial.getSelection();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return aryData;
	}

	public static String simpleGetRedLine(IRow iRow, Integer baseID) {
		String strData = null;

		try {
			strData = iRow.getValue(baseID).toString();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return strData;
	}
}
