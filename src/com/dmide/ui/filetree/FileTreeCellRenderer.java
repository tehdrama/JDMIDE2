package com.dmide.ui.filetree;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTreeCellRenderer;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FilenameUtils;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import com.dmide.util.IDEFile;
import com.dmide.util.misc.Misc;

@SuppressWarnings("serial")
public class FileTreeCellRenderer implements CheckboxTreeCellRenderer {

	FileTree fileTree;

	CustomTreeCheckbox checkBox = new CustomTreeCheckbox();
	JLabel label = new JLabel();
	JPanel panel;

	ArrayList<String> checkbox_extensions = new ArrayList<>();

	boolean jp_checkbox = false;
	boolean jp_label = false;

	//Border selectedLabelBorder;
	//Border unselectedLabelBorder;

	public void jpanelSettings(boolean jp_c, boolean jp_l) {
		if(this.jp_checkbox != jp_c) {
			this.jp_checkbox = jp_c;
			if(this.jp_checkbox) {
				//this.panel.add(this.checkBox, 0);
				this.checkBox.setVisible(true);
			} else {
				//this.panel.remove(this.checkBox);
				this.checkBox.setVisible(false);
			}
		}
		if(this.jp_label != jp_l) {
			this.jp_label = jp_l;
			if(this.jp_label) {
				//this.panel.add(this.label, 1);
				this.label.setVisible(true);
			} else {
				//this.panel.remove(this.label);
				this.label.setVisible(false);
			}
		}
	}

	public FileTreeCellRenderer(FileTree fileTree) {
		this.fileTree = fileTree;
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT, 0, 0);
		this.panel = new JPanel(fl);
		this.panel.add(this.checkBox);
		this.panel.add(this.label);
		this.checkBox.putClientProperty(LafWidget.COMPONENT_PREVIEW_PAINTER, false);


		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ARM, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.FOCUS, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.FOCUS_LOOP_ANIMATION, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_BUTTON_PRESS, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_ICON_ROLLOVER, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ICON_GLOW, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.PRESS, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.ROLLOVER, this.checkBox);
		AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.SELECTION, this.checkBox);
		this.checkBox.setRolloverEnabled(false);
		this.checkBox.setRolloverIcon(null);
		this.checkBox.setRolloverSelectedIcon(null);

		this.label.setOpaque(true);

		//this.selectedLabelBorder = new LineBorder(
		//		SubstanceLookAndFeel.getCurrentSkin().
		//		getColorScheme(this.label, ComponentState.DEFAULT).getUltraDarkColor());
		//this.unselectedLabelBorder = null;

		Collections.addAll(this.checkbox_extensions, "dm", "dms", "dmm", "dmp", "dmf");
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
	        boolean isExpanded, boolean isLeaf, int row, boolean hasFocus) {

		if (isSelected) {
			this.label.setBackground(
					SubstanceLookAndFeel.getCurrentSkin().
					getColorScheme(this.label, ComponentState.DEFAULT).getExtraLightColor());
			//this.label.setBorder(this.selectedLabelBorder);
			//this.label.setBackground(UIManager.getColor("Tree.selectionBackground"));
		} else {
			this.label.setBackground(UIManager.getColor("Tree.textBackground"));
			//this.label.setBorder(this.unselectedLabelBorder);
		}

		TreeCheckingModel checkingModel = ((CheckboxTree)
				tree).getCheckingModel();
			TreePath path = tree.getPathForRow(row);
		boolean enabled = checkingModel.isPathEnabled(path);
		boolean checked = checkingModel.isPathChecked(path);
		boolean grayed = checkingModel.isPathGreyed(path);
		this.checkBox.setEnabled(enabled);
		if (grayed) {
			this.label.setForeground(Color.lightGray);
		} else {
			this.label.setForeground(UIManager.getColor("Tree.textForeground"));
		}
		this.checkBox.setSelected(checked);

		if(value instanceof IDEFile) {
			IDEFile ideFile = (IDEFile)value;
			File sfile = ideFile.getFileObject();
			if(this.fileTree != null && !sfile.isAbsolute()) {
				sfile = this.fileTree.getAbsoluteFile(ideFile);
			}
			if(sfile.isDirectory() && this.fileTree != null) {
				if(this.fileTree.isExpanded(new TreePath(ideFile.getPath()))) {
					this.label.setIcon(this.fileTree.getDirOpenedIcon());
				} else {
					this.label.setIcon(this.fileTree.getDirClosedIcon());
				}
			} else {
				this.label.setIcon(Misc.io.getFileTypeIcon(sfile));
			}
			this.label.setText(sfile.getName());
			this.jpanelSettings(this.checkbox_extensions.contains(FilenameUtils.getExtension(sfile.getName())), true);
		} else {
			this.label.setText(value.toString());
			this.jpanelSettings(true, true);
		}


		return this.panel;

	}

	@Override
	public boolean isOnHotspot(int x, int y) {
		return (this.checkBox.getBounds().contains(x, y));
	}

}
