/**
 * 
 */
package report;

import java.util.LinkedList;

import parameter.IAlgorithmParameter;
import parameter.ID3ParameterConstants.ID3ParameterEnum;
import datastructure.ID3Node;

/**
 * @author sumit
 *
 */
public class ID3ReportGenerator {

	public static void generateReport(
			IAlgorithmParameter<ID3ParameterEnum, Object> algoStatsContainer) {
		/*
		 * System.out.println(algoStatsContainer
		 * .getAlgorithmParameter(ID3ParameterEnum.OUTPUT_OF_ALGORITHM));
		 * System.out.println(((ID3Node) algoStatsContainer
		 * .getAlgorithmParameter(ID3ParameterEnum.OUTPUT_OF_ALGORITHM))
		 * .getChildrens());
		 */
		printLevelOrder((ID3Node) algoStatsContainer
				.getAlgorithmParameter(ID3ParameterEnum.OUTPUT_OF_ALGORITHM));
	}

	public static void printLevelOrder(ID3Node root) {
		if (root == null) {
			System.out.println("No Tree to print!");
			return;
		}
		LinkedList<ID3Node> queue = new LinkedList<>();
		queue.offer(root);
		int size = 1;
		int level = 0;
		ID3Node tempNode = null;
		while (!queue.isEmpty()) {
			System.out.println("-------------- Tree Node at level : " + level
					+ "-----------------");
			size = queue.size();
			while (size > 0) {
				tempNode = queue.poll();
				size--;
				System.out.println(tempNode);
				if (tempNode.getChildrens() == null)
					continue;
				for (ID3Node x : tempNode.getChildrens()) {
					queue.offer(x);
				}

			}
			level++;
		}

	}
}
