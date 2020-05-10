package com.ybs.ecom.log.ervice;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EcomLogExtractor {

	String errorMsg;
	File file;
	private static final int TXT_AREA_ROWS = 10;
	private static final int TXT_AREA_COLS = 50;

	public EcomLogExtractor() {
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		try {
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFileChooser fc = new JFileChooser();
		// POIFSFileSystem fs = null;
		JFrame frame = new JFrame("Application to extract data from log file");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel headingPanel = new JPanel();

		GridBagConstraints constv = new GridBagConstraints();
		constv.insets = new Insets(5, 5, 5, 5);
		constv.anchor = GridBagConstraints.WEST;
		constv.gridx = 0;
		constv.gridy = 0;
		// create the JTextArea, passing in the rows and columns values
		JTextArea textArea = new JTextArea(TXT_AREA_ROWS, TXT_AREA_COLS);
		// create the JScrollPane, adding our JTextArea
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// Panel to define the layout. We are using GridBagLayout
		JPanel panel = new JPanel(new GridBagLayout());
		// Constraints for the layout
		GridBagConstraints constr = new GridBagConstraints();
		constr.insets = new Insets(5, 5, 5, 5);
		constr.anchor = GridBagConstraints.WEST;

		// Set the initial grid values to 0,0
		constr.gridx = 0;
		constr.gridy = 0;

		JLabel label1 = new JLabel("");
		JLabel labelVal1 = new JLabel("");
		JLabel label2 = new JLabel("Enter customer\\account\\partsysid ::");
		JTextField txtVal2 = new JTextField(20);
		JLabel label3 = new JLabel("Enter session id ::");
		JTextField txtVal3 = new JTextField(20);
		JLabel label4 = new JLabel("");
		JLabel labelVal4 = new JLabel("");
		JButton readButton = new JButton("Choose a File");
		StringBuilder buffer = new StringBuilder();

		readButton.addActionListener(ev -> {
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				try {
					@SuppressWarnings("resource")
					BufferedReader br = new BufferedReader(new FileReader(file));
					String st;
					while ((st = br.readLine()) != null) {
						buffer.append(st).append("\n");
					}

					label4.setText("");
					labelVal4.setText("");
					label1.setText("Log file selected :: ");
					labelVal1.setText(file.getCanonicalPath());
					label2.setText("Enter customer\\account\\partsysid :: ");
					label3.setText("Enter session id |S:x| :: ");

				} catch (Exception e) {
					label1.setText("");
					labelVal1.setText("");
					label2.setText("");

					errorMsg = e.getLocalizedMessage();
					textArea.setText("Error::" + errorMsg);
				}
			} else {
				label1.setText("");
				labelVal1.setText("");
				textArea.setText("Operation is CANCELLED :(");
			}
		});

		JButton readButton1 = new JButton("Extract log");
		readButton1.addActionListener(ev -> {
			BufferedWriter out;
			try {
				int cusNumber=Integer.parseInt(txtVal2.getText());
				String sessionVal = txtVal2.getText();
				out = new BufferedWriter(new FileWriter(file.getAbsoluteFile().getParent()+ cusNumber+"--" + file.getName()));
				out.write(buffer.toString());
				label4.setText("Extracted Log saved at:: ");
				labelVal4.setText(file.getAbsoluteFile().getParent()+ cusNumber+"--" + file.getName());
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		headingPanel.add(new JLabel("Select log file ::   "), constv);
		constv.gridx = 1;
		headingPanel.add(readButton, constv);
		constv.gridx = 0;
		constv.gridy = 2;

		panel.add(label1, constr);
		constr.gridx = 1;
		panel.add(labelVal1, constr);
		constr.gridx = 0;
		constr.gridy = 1;

		panel.add(label2, constr);
		constr.gridx = 1;
		panel.add(txtVal2, constr);
		constr.gridx = 0;
		constr.gridy = 2;

		panel.add(label3, constr);
		constr.gridx = 1;
		panel.add(txtVal3, constr);
		constr.gridx = 0;
		constr.gridy = 3;

		panel.add(label4, constr);
		constr.gridx = 1;
		panel.add(labelVal4, constr);
		constr.gridx = 0;
		constr.gridy = 4;

		panel.add(readButton1, constr);
		constr.gridx = 0;
		constr.gridy = 5;

		mainPanel.add(headingPanel);
		mainPanel.add(panel);
		mainPanel.add(scroll);
		// Add panel to frame
		frame.add(mainPanel);

		frame.pack();
		frame.setSize(500, 300);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new EcomLogExtractor();
	}

}

