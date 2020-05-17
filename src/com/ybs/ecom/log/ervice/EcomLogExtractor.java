package com.ybs.ecom.log.ervice;

import java.awt.BorderLayout;
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
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	String errorMsg = null;
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

		JLabel label1 = new JLabel("Log file selected ::");
		JLabel labelVal1 = new JLabel("");
		JLabel label2 = new JLabel("Enter customer\\account\\partsysid ::");
		JTextField txtVal2 = new JTextField(12);

		JLabel label4 = new JLabel("");
		JLabel labelVal4 = new JLabel("");

		JLabel label5 = new JLabel("");
		JLabel labelVal5 = new JLabel("");

		JLabel label6 = new JLabel("");
		JLabel labelVal6 = new JLabel("");

		JButton readButton = new JButton("Choose a File");
		StringBuilder buffer = new StringBuilder();
		StringBuilder pageFlowBuffer = new StringBuilder();
		StringBuilder errorBuffer = new StringBuilder();
		ArrayList<String> ans = new ArrayList<String>();

		readButton.addActionListener(ev -> {
			errorMsg = null;
			
			BufferedReader buf = null;
			buffer.delete(0, buffer.length());
			pageFlowBuffer.delete(0, pageFlowBuffer.length());
			errorBuffer.delete(0, errorBuffer.length());
			ans.clear();
			int returnVal = fc.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				try {
					// @SuppressWarnings("resource")
					buf = new BufferedReader(new FileReader(file));
					String st2 = null;
					while ((st2 = buf.readLine()) != null) {
						ans.add(st2);
					}

					label1.setText("Log file selected :: ");
					labelVal1.setText(file.getCanonicalPath());
					txtVal2.setText("");
					label4.setText("");
					labelVal4.setText("");
					label5.setText("");
					labelVal5.setText("");
					label6.setText("");
					labelVal6.setText("");

					textArea.setText("");

				} catch (Exception e) {
					label1.setText("");
					labelVal1.setText("");
					//label2.setText("");
					label4.setText("");
					labelVal4.setText("");
					label5.setText("");
					labelVal5.setText("");
					label6.setText("");
					labelVal6.setText("");

					errorMsg = e.getLocalizedMessage();
					textArea.setText("Error::" + errorMsg);
				}
			} else {
				label1.setText("");
				labelVal1.setText("");
				txtVal2.setText("");
				label4.setText("");
				labelVal4.setText("");
				label5.setText("");
				labelVal5.setText("");
				label6.setText("");
				labelVal6.setText("");
				file=null;
				textArea.setText("Operation is CANCELLED :(");
			}
		});

		JButton readButton1 = new JButton("Extract log");
		readButton1.addActionListener(ev -> {
			errorMsg = null;
			BufferedWriter out = null;
			BufferedWriter pageFlowOut=null;
			BufferedWriter errorOut=null;
			int cusNumber=0;
			if(file==null) {
				errorMsg = "Please select log file \n";
				textArea.setText(" Error::" + errorMsg);
			}
			if(txtVal2.getText().equals("")) {
				if(errorMsg!=null) {
				errorMsg = errorMsg+"Please enter customer\\account\\partsysid ";
				}else {
					errorMsg = "Please enter customer\\account\\partsysid ";
				}
				label4.setText("");
				labelVal4.setText("");
				label5.setText("");
				labelVal5.setText("");
				label6.setText("");
				labelVal6.setText("");
				textArea.setText(" Error::" + errorMsg);
			} 
			if(errorMsg==null){
		      cusNumber= Integer.parseInt(txtVal2.getText());
			
			File f = new File(file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-" + file.getName());
			if(f.exists()){
				f.delete();
			}
			File f1 = new File(file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-PageFlow-" + file.getName());
			if(f1.exists()){
				f1.delete();
			}
			File f2 = new File(file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-Error-" + file.getName());
			if(f2.exists()){
				f2.delete();
			}
			
			try {
				label4.setText("");
				labelVal4.setText("");
				label5.setText("");
				labelVal5.setText("");
				label6.setText("");
				labelVal6.setText("");

				textArea.setText("");
				
				// String sessionVal = txtVal2.getText();

				List<String> sessionId = new ArrayList<String>();
				SortedSet<String> targetSet = null;

				for (String st : ans) {
					if (st.contains(txtVal2.getText())) {
						Pattern p = Pattern.compile(".*\\| *(.*) *\\|.*");
						Matcher m = p.matcher(st);
						if (m.find()) {
							String text = m.group(1);

							if (text.startsWith("S:")) {
								sessionId.add(text);
							}

						}
					}

				}
				if (sessionId.size() == 0) {
					errorMsg = "No information available \n Please check brand, date of incident \n and try again";
					textArea.setText(" Error::" + errorMsg);
				}
				/*
				 * String strVal = null; for (String st1 : ans) { if
				 * (st1.contains(sessionId.get(0))) { int index = st1.indexOf("Headers for"); if
				 * (index > 0) { // if(st1.contains(st1.substring(index+13))); //
				 * System.out.println("**********"+strVal+"***********"); strVal =
				 * st1.substring(index + 13); break; } }
				 * 
				 * }
				 */

				/*
				 * for (String st1 : ans) { if (st1.contains(strVal)) { Pattern p =
				 * Pattern.compile(".*\\| *(.*) *\\|.*"); Matcher m = p.matcher(st1); if
				 * (m.find()) { String text = m.group(1);
				 * 
				 * if (text.startsWith("S:")) { sessionId.add(text); }
				 * 
				 * }
				 * 
				 * } }
				 */

				// System.out.println(sessionId);
				targetSet = new TreeSet<>(sessionId);
				// System.out.println(targetSet);
				// String st1;
				if (errorMsg == null) {
					for (String setVal : targetSet) {

						for (String st1 : ans) {
							if (st1.contains(setVal)) {
								buffer.append(st1).append("\n");

							}

							if (st1.contains(setVal) && st1.contains("RequestLoggingFilter")) {
								pageFlowBuffer.append(st1).append("\n");
							}

							if (st1.contains(setVal) && st1.contains("Headers for")) {
								boolean val = true;
								int index = ans.indexOf(st1);

								while (val) {
									Pattern p = Pattern.compile(".*\\| *(.*) *\\|.*");
									Matcher m = p.matcher(ans.get(++index));
									if (m.find()) {
										String text = m.group(1);

										if (text.startsWith("S:")) {
											val = false;
										}

									} else {
										buffer.append(ans.get(index)).append("\n");
									}
								}
							}

							if (st1.contains(setVal)
									&& st1.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")) {
								boolean val = true;
								int index = ans.indexOf(st1);

								while (val) {
									Pattern p = Pattern.compile(".*\\| *(.*) *\\|.*");
									Matcher m = p.matcher(ans.get(++index));
									if (m.find()) {
										String text = m.group(1);

										if (text.startsWith("S:")) {
											val = false;
										}

									} else {
										buffer.append(ans.get(index)).append("\n");
									}
								}
							}

							if (st1.contains(setVal) && st1.contains("ERROR")) {
								boolean val = true;
								int index = ans.indexOf(st1);
								errorBuffer.append(st1).append("\n");

								while (val) {
									Pattern p = Pattern.compile(".*\\| *(.*) *\\|.*");
									Matcher m = p.matcher(ans.get(++index));
									if (m.find()) {
										String text = m.group(1);

										if (text.startsWith("S:")) {
											val = false;
										}

									} else {
										buffer.append(ans.get(index)).append("\n");
										errorBuffer.append(ans.get(index)).append("\n");
									}
								}
							}

						}
					}
					
					

					out = new BufferedWriter(new FileWriter(
							file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-" + file.getName()));
					pageFlowOut = new BufferedWriter(new FileWriter(
							file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-PageFlow-" + file.getName()));
					errorOut = new BufferedWriter(new FileWriter(
							file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-Error-" + file.getName()));
					out.write(buffer.toString());
					buffer.delete(0, buffer.length());


					out.close();

					pageFlowOut.write(pageFlowBuffer.toString());
					pageFlowBuffer.delete(0, pageFlowBuffer.length());
					pageFlowOut.close();

					errorOut.write(errorBuffer.toString());
					errorBuffer.delete(0, errorBuffer.length());
					errorOut.close();

					label4.setText("Extracted Full Customer Logs saved at:: ");
					labelVal4.setText(file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-" + file.getName());
					label5.setText("Extracted Page Flow Logs saved at:: ");
					labelVal5.setText(
							file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-PageFlow-" + file.getName());
					label6.setText("Extracted Error Logs saved at:: ");
					labelVal6.setText(
							file.getAbsoluteFile().getParent() + "\\" + cusNumber + "-Error-" + file.getName());

					textArea.setText(" Successfully completed :)");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorMsg = e.getLocalizedMessage();
				textArea.setText("Error::" + errorMsg);
			}

			}});

		headingPanel.add(new JLabel("Select log file ::   "), constv);
		constv.gridx = 1;
		headingPanel.add(readButton, constv);
		constv.gridx = 0;
		constv.gridy = 2;

		panel.add(label1, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		
		constr.gridx = 1;
		panel.add(labelVal1, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 0;
		constr.gridy = 1;

		panel.add(label2, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 1;
		panel.add(txtVal2, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 0;
		constr.gridy = 2;

		panel.add(label4, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 1;
		panel.add(labelVal4, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 0;
		constr.gridy = 3;

		panel.add(label5, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 1;
		panel.add(labelVal5, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 0;
		constr.gridy = 4;

		panel.add(label6, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 1;
		panel.add(labelVal6, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 0;
		constr.gridy = 5;

		panel.add(readButton1, constr);
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.gridx = 0;
		constr.gridy = 4;

		mainPanel.add(headingPanel);
		mainPanel.add(panel);
		mainPanel.add(scroll);
		// Add panel to frame
		frame.add(mainPanel,BorderLayout.CENTER);

		// frame.pack();
		frame.setSize(700, 400);
		//frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new EcomLogExtractor();
	}

}
