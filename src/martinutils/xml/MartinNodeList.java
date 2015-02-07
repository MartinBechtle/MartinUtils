package martinutils.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Rende una w3c.dom.NodeList iterabile, per comodità. E' supportato da una arraylist con tutti i nodi della NodeList.
 * Questa chiaramente viene popolata in tempo lineare, quindi c'è un overhead seppur minimo. Evitare dunque questa
 * classe quando le performance sono assolutamente critiche.
 * @author martin
 */
public class MartinNodeList implements Iterable<Node>
{
	private List<Node> nodeList;
	
	public MartinNodeList(NodeList nodeList)
	{
		if (nodeList == null)
			throw new IllegalArgumentException("nodeList cannot be null");
		
		this.nodeList = new ArrayList<>();
		int len = nodeList.getLength();
		
		for (int i = 0; i < len; i++)
			this.nodeList.add( nodeList.item(i) );
	}
	
	@Override
	public Iterator<Node> iterator()
	{
		return nodeList.iterator();
	}
	
	public boolean isEmpty()
	{
		return nodeList.isEmpty();
	}
	
	public int size()
	{
		return nodeList.size();
	}
}