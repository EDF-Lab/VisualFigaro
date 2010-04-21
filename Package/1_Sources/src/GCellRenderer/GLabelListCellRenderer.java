package GCellRenderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class GLabelListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID    = 1L;
   
    public GLabelListCellRenderer(){
        super();
    }
   
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
       
        if(JLabel.class.isInstance(value)) {
        	
        	((JLabel)value).setOpaque(true);
        	
        	if(isSelected) {
        		((JLabel)value).setBackground(list.getSelectionBackground());
        		((JLabel)value).setForeground(list.getSelectionForeground());
        	} else {
        		((JLabel)value).setBackground(list.getBackground());
        		((JLabel)value).setForeground(list.getForeground());
        	}
        	
            return ((JLabel)value);
        }
       
        return super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
    }
   
}