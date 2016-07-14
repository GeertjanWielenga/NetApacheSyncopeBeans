package com.nasb;

import java.io.IOException;
import javax.swing.Action;
import org.netbeans.api.core.ide.ServicesTabNodeRegistration;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.NewAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;

@ServicesTabNodeRegistration(
        displayName = "#LBL_Items",
        iconResource = "com/nasb/syncope.png",
        name = "#LBL_Items")
@NbBundle.Messages({"LBL_Items=Apache Syncope"})
public class SyncopeNode extends AbstractNode {

    public SyncopeNode() {
        super(Children.create(new SyncopeChildFactory(), true));
        setDisplayName(Bundle.LBL_Items());
        setShortDescription(Bundle.LBL_Items());
        setIconBaseWithExtension("com/nasb/syncope.png");
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{SystemAction.get(NewAction.class)};
    }

    @NbBundle.Messages({
        "LBL_Title=Item",
        "LBL_Text=Enter Item Name:"})
    @Override
    public NewType[] getNewTypes() {
        return new NewType[]{
            new NewType() {
                @Override
                public String getName() {
                    return Bundle.LBL_Title();
                }

                @Override
                public void create() throws IOException {
                    NotifyDescriptor.InputLine msg = new NotifyDescriptor.InputLine(
                            Bundle.LBL_Text(),
                            Bundle.LBL_Title());
                    Object result = DialogDisplayer.getDefault().notify(msg);
                    if (result == NotifyDescriptor.OK_OPTION) {
                        String itemName = msg.getInputText();
                        NbPreferences.forModule(SyncopeNode.class).
                                put("itemName", itemName);
                        PropertiesNotifier.changed();
                    }
                }
            }
        };
    }
    
}
