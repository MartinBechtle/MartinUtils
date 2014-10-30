package martinutils.text;

import java.util.LinkedList;

import martinutils.text.DiffManager.Diff;

public class Comparison
{
	public static String computeHtmlComparison(String oldStr, String newStr, boolean cleanEntities)
	{
		if (cleanEntities)
		{
			oldStr = HTMLEntities.unhtmlentities(oldStr);
			newStr = HTMLEntities.unhtmlentities(newStr);
		}
		
		DiffManager diff = new DiffManager();
		LinkedList<Diff> list = diff.diff_main(oldStr, newStr, false);
		diff.diff_cleanupSemantic(list);
		String html = diff.diff_prettyHtml(list);
		return html;
	}
}
