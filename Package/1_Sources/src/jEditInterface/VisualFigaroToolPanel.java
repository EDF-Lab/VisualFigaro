package jEditInterface;

/**
 * @author Guillaume Torrente & Marc Bouissou
 */

/*
 * VisualFigaroToolPanel.java
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

import javax.swing.*;

public class VisualFigaroToolPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public VisualFigaroToolPanel(VisualFigaro vFig)
	{
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	}
}

