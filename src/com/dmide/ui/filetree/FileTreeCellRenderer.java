package com.dmide.ui.filetree;

import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.dmide.util.IDEFile;
import com.dmide.util.misc.Misc;

@SuppressWarnings("serial")
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	FileTree fileTree;

	public FileTreeCellRenderer(FileTree fileTree) {
		this.fileTree = fileTree;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
	        boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {
		Component rtnComp = super.getTreeCellRendererComponent(tree, value, isSelected,
				isExpanded, isLeaf, row, hasFocus);
		if(rtnComp instanceof JLabel) {
			if(value instanceof IDEFile) {
				JLabel label = (JLabel) rtnComp;
				IDEFile ideFile = (IDEFile)value;
				File sfile = ideFile.getFileObject();
				if(this.fileTree != null && !sfile.isAbsolute()) {
					sfile = this.fileTree.getAbsoluteFile(ideFile);
				}
				if(sfile.isDirectory() && this.fileTree != null) {
					if(this.fileTree.isExpanded(new TreePath(ideFile.getPath()))) {
						label.setIcon(this.fileTree.getDirOpenedIcon());
					} else {
						label.setIcon(this.fileTree.getDirClosedIcon());
					}
				} else {
					label.setIcon(Misc.io.getFileTypeIcon(sfile));
				}
			}
		}
		return rtnComp;
	}

}
