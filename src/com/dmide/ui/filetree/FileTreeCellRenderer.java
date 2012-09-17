package com.dmide.ui.filetree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

@SuppressWarnings("serial")
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
	        boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
		Component rtnComp = super.getTreeCellRendererComponent(tree, value, isSelected,
				isExpanded, isLeaf, row, hasFocus);
		if(rtnComp instanceof JLabel) {
			JLabel label = (JLabel) rtnComp;
		}
		return rtnComp;
	}

}
