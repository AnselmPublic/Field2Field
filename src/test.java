import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.agile.api.APIException;
import com.agile.api.AgileSessionFactory;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileClass;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.IAttribute;
import com.agile.api.ICell;
import com.agile.api.IChange;
import com.agile.api.IDataObject;
import com.agile.api.IItem;
import com.agile.api.IManufacturer;
import com.agile.api.IManufacturingSite;
import com.agile.api.IRow;
import com.agile.api.ITable;
import com.agile.api.ITwoWayIterator;
import com.agile.api.ItemConstants;

import MainForm.FieldToField;
import library.GetLibrary;

public class test {

	public static void main(String[] args) {
		IAgileSession session = agileAPConn("http://192.168.88.134:7001/Agile/", "admin", "agile936");
		try {
			IDataObject iDataObject = (IDataObject) session.getObject(IItem.OBJECT_TYPE, "TEMP_000004");
			System.out.println(iDataObject.getName());
			ICell iCellGet = iDataObject.getCell(1564);
			IAgileList[] iAgileList = GetLibrary.listGet(iDataObject, 1564);

			for (int i = 0; i < iAgileList.length; i++) {
				System.out.println(iAgileList[i].toString());
			}

		} catch (APIException e) {
			System.out.println(e);
			if (e.getErrorCode().equals(60018)) {
				System.out.println("***");
			}
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		} finally {
			session.close();
		}
	}

	public static IAgileSession agileAPConn(String url, String uid, String upw) {
		IAgileSession session = null;
		try {
			HashMap params = new HashMap();
			// Put username, password, and URL values into params
			params.put(AgileSessionFactory.USERNAME, uid);
			params.put(AgileSessionFactory.PASSWORD, upw);
			params.put(AgileSessionFactory.URL, url);
			// Create the Agile PLM session and log in return
			AgileSessionFactory factory = AgileSessionFactory.refreshInstanceEx(params);
			session = factory.createSession(params);
			System.out.println(">>> Agile AP is connected.");

			return session;
		} catch (Exception e) {
			System.out.println(">>>Agile AP connect error!!");
			e.printStackTrace();
			return null;
		}
	}
}
