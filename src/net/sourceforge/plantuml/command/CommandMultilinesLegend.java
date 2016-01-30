/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 10779 $
 *
 */
package net.sourceforge.plantuml.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.VerticalAlignment;

public class CommandMultilinesLegend extends CommandMultilines2<UmlDiagram> {

	public CommandMultilinesLegend() {
		super(getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("legend"), //
				new RegexLeaf("VALIGN", "(?:[%s]+(top|bottom))?"), //
				new RegexLeaf("ALIGN", "(?:[%s]+(left|right|center))?"), //
				new RegexLeaf("$"));
	}

	@Override
	public String getPatternEnd() {
		return "(?i)^end[%s]?legend$";
	}

	@Override
	public CommandExecutionResult executeNow(UmlDiagram diagram, BlocLines lines) {
		lines = lines.trimSmart(1);
		final RegexResult line0 = getStartingPattern().matcher(StringUtils.trin(lines.getFirst499()));
		final String align = line0.get("ALIGN", 0);
		final String valign = line0.get("VALIGN", 0);
		lines = lines.subExtract(1, 1);
		lines = lines.removeEmptyColumns();
		final Display strings = lines.toDisplay();
		if (strings.size() > 0) {
			final VerticalAlignment valignment = VerticalAlignment.fromString(valign);
			HorizontalAlignment alignment = HorizontalAlignment.fromString(align);
			if (alignment == null) {
				alignment = HorizontalAlignment.CENTER;
			}
			diagram.setLegend(new DisplayPositionned(strings, alignment, valignment));
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("No legend defined");
	}
}