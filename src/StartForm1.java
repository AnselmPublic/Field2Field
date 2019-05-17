import com.agile.api.IAgileSession;
import com.agile.api.IDataObject;
import com.agile.api.INode;
import com.agile.px.ActionResult;
import com.agile.px.ICustomAction;

import com.anselm.tools.record.Ini;
import com.anselm.tools.record.Log;

import MainForm.FieldToField;

public class StartForm1 implements ICustomAction {
	String[] aryField = new String[] { "Item,1575:Item,1576", "Item,1564:Item,1565" };

	@Override
	public ActionResult doAction(IAgileSession iAgileSession, INode iNode, IDataObject ido) {
		/** It is used for config reading and writing. */
		Ini ini = null;
		Log log = null;

		try {
			// Step 1. Initialize log file
			ini = new Ini();
			log = new Log();

			/* Record of log path */
			String strLogfilepath = ini.getValue("Program Use", "LogFile");

			log.setLogFile(strLogfilepath + "/TestField2Field.log", ini);
			log.logSeparatorBar();

			// Set Start field to field class
			FieldToField fieldToField = new FieldToField();

			// do action one by one
			for (int i = 0; i < aryField.length; i++) {
				log.logger(aryField[i]);
				String[] ary2Field = separate(aryField[i]);

				if (ary2Field.length >= 2) {
					fieldToField.judge2Field(ido, ary2Field[0], ary2Field[1], log, ini);
				} else {
					return new ActionResult(ActionResult.STRING, "Wrong input array[], maybe forget \":\"");
				}
			}

			log.close();
		} catch (Exception e) {
			log.logException(e);
		}
		return new ActionResult(ActionResult.STRING, "");
	}

	private String[] separate(String _string) {
		/* For return separate details */
		String[] aryDetails = null;

		aryDetails = _string.trim().split(":");
		return aryDetails;
	}
}
