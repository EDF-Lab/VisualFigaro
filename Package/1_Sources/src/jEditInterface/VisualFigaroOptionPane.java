package jEditInterface;

/**
 * @author Guillaume Torrente & Marc Bouissou
 */

/*
 * VisualFigaroOptionPane.java
 * part of the VisualFigaro plugin for the jEdit text editor
 * Copyright (C) 2008 Guillaume Torrente & Marc Bouissou
 * guillaumetorrente@yahoo.fr
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.gui.FontSelector;

public class VisualFigaroOptionPane extends AbstractOptionPane implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @uml.property  name="showPath"
	 * @uml.associationEnd  
	 */
	private JCheckBox showPath;
	/**
	 * @uml.property  name="pathName"
	 * @uml.associationEnd  
	 */
	private JTextField pathName;
	/**
	 * @uml.property  name="font"
	 * @uml.associationEnd  
	 */
	private FontSelector font;

	public VisualFigaroOptionPane()
	{
		super(VisualFigaroPlugin.NAME);
	}

	public void _init()
	{
		showPath = new JCheckBox(jEdit.getProperty(
				VisualFigaroPlugin.OPTION_PREFIX + "show-filepath.title"),
			jEdit.getProperty(VisualFigaroPlugin.OPTION_PREFIX +
				"show-filepath").equals("true"));
		addComponent(showPath);

		
		pathName = new JTextField(jEdit.getProperty(
				VisualFigaroPlugin.OPTION_PREFIX + "filepath"));
		JButton pickPath = new JButton(jEdit.getProperty(
				VisualFigaroPlugin.OPTION_PREFIX + "choose-file"));
		pickPath.addActionListener(this);
		

		JPanel pathPanel = new JPanel(new BorderLayout(0, 0));
		pathPanel.add(pathName, BorderLayout.CENTER);
		pathPanel.add(pickPath, BorderLayout.EAST);

		addComponent(jEdit.getProperty(
				VisualFigaroPlugin.OPTION_PREFIX + "file"),
			pathPanel);

		font = new FontSelector(makeFont());
		addComponent(jEdit.getProperty(
				VisualFigaroPlugin.OPTION_PREFIX + "choose-font"),
			font);
	}

	public void _save()
	{
		jEdit.setProperty(VisualFigaroPlugin.OPTION_PREFIX + "filepath",
			pathName.getText());
		Font _font = font.getFont();
		jEdit.setProperty(VisualFigaroPlugin.OPTION_PREFIX + "font",
			_font.getFamily());
		jEdit.setProperty(VisualFigaroPlugin.OPTION_PREFIX + "fontsize",
			String.valueOf(_font.getSize()));
		jEdit.setProperty(VisualFigaroPlugin.OPTION_PREFIX + "fontstyle",
			String.valueOf(_font.getStyle()));
		jEdit.setProperty(VisualFigaroPlugin.OPTION_PREFIX + "show-filepath",
			String.valueOf(showPath.isSelected()));
	}
	// end AbstractOptionPane implementation

	// begin ActionListener implementation
	public void actionPerformed(ActionEvent evt)
	{
		String[] paths = GUIUtilities.showVFSFileDialog(null,
			null,JFileChooser.OPEN_DIALOG,false);
		if(paths != null)
		{
			pathName.setText(paths[0]);
		}
	}

	// helper method to get Font from plugin properties
	static public Font makeFont()
	{
		int style, size;
		String family = jEdit.getProperty(
				VisualFigaroPlugin.OPTION_PREFIX + "font");
		try
		{
			size = Integer.parseInt(jEdit.getProperty(
					VisualFigaroPlugin.OPTION_PREFIX + "fontsize"));
		}
		catch(NumberFormatException nf)
		{
			size = 14;
		}
		try
		{
			style = Integer.parseInt(jEdit.getProperty(
					VisualFigaroPlugin.OPTION_PREFIX + "fontstyle"));
		}
		catch(NumberFormatException nf)
		{
			style = Font.PLAIN;
		}
		return new Font(family, style, size);
	}

}

