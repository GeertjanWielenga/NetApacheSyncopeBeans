package com.nasb;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public class SyncopeChildFactory extends ChildFactory.Detachable<String> {
    
    private final List<String> itemNames;
    private ChangeListener listener;
    public SyncopeChildFactory() {
        this.itemNames = new ArrayList<String>();
    }
    @Override
    protected void addNotify() {
        PropertiesNotifier.addChangeListener(listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ev) {
                String itemName = 
                        NbPreferences.forModule(SyncopeNode.class).
                                get("itemName", "error!");
                itemNames.add(itemName);
                refresh(true);
            }
        });
    }
    @Override
    protected void removeNotify() {
        if (listener != null) {
            PropertiesNotifier.removeChangeListener(listener);
            listener = null;
        }
    }
    @Override
    protected boolean createKeys(List<String> list) {
        list.addAll(itemNames);
        return true;
    }
    @Override
    protected Node createNodeForKey(String key) {
        BeanNode node = null;
        try {
            node = new BeanNode(key);
            node.setDisplayName(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }
}