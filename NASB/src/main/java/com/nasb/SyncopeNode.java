package com.nasb;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.netbeans.api.core.ide.ServicesTabNodeRegistration;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.actions.NewAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Cancellable;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.NewType;

@ServicesTabNodeRegistration(
        displayName = "#LBL_Items",
        iconResource = "com/nasb/syncope.png",
        name = "#LBL_Items")
@NbBundle.Messages({"LBL_Items=Apache Syncope"})
public class SyncopeNode extends AbstractNode {

    private MailTemplateManagerService mailTemplateManagerService;
    private ReportTemplateManagerService reportTemplateManagerService;
    private Charset encodingPattern;

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

                    File file = new File("UserData.txt");
                    if (!file.exists()) {
                        new ServerDetailsView(null, true).setVisible(true);
                    }
                    try {
                        mailTemplateManagerService = ResourceConnector.getMailTemplateManagerService();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error Occured.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        new ServerDetailsView(null, true).setVisible(true);
                    }
                    try {
                        reportTemplateManagerService
                                = ResourceConnector.getReportTemplateManagerService();
                    } catch (IOException ex) {
                        new ServerDetailsView(null, true).setVisible(true);
                    }

                    Runnable tsk = new Runnable() {
                        @Override
                        public void run() {
                            final ProgressHandle progr = ProgressHandleFactory.createHandle("Loading Templates", new Cancellable() {
                                @Override
                                public boolean cancel() {
                                    return true;
                                }
                            }, new Action() {
                                @Override
                                public Object getValue(String key) {
                                    return null;
                                }

                                @Override
                                public void putValue(String key, Object value) {
                                }

                                @Override
                                public void setEnabled(boolean b) {
                                }

                                @Override
                                public boolean isEnabled() {
                                    return false;
                                }

                                @Override
                                public void addPropertyChangeListener(PropertyChangeListener listener) {
                                }

                                @Override
                                public void removePropertyChangeListener(PropertyChangeListener listener) {
                                }

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                }
                            });

                            progr.start();
                            progr.progress("Loading Templates.");
//                            addMailTemplates();
//                            addReportXslts();
                            progr.finish();
                        }

                    };
                    RequestProcessor.getDefault().post(tsk);

//                    NotifyDescriptor.InputLine msg = new NotifyDescriptor.InputLine(
//                            Bundle.LBL_Text(),
//                            Bundle.LBL_Title());
//                    Object result = DialogDisplayer.getDefault().notify(msg);
//                    if (result == NotifyDescriptor.OK_OPTION) {
//                        String itemName = msg.getInputText();
//                        NbPreferences.forModule(SyncopeNode.class).
//                                put("itemName", itemName);
//                        PropertiesNotifier.changed();
//                    }
                }
            }
        };
    }

}
