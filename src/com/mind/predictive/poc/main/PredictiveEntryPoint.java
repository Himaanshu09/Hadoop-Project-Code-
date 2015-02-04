package com.mind.predictive.poc.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.mind.predictive.poc.mr.util.MRConstants;
import com.mind.predictive.poc.predictors.AggregateDefects;
import com.mind.predictive.poc.predictors.AggregateFuel;
import com.mind.predictive.poc.predictors.AggregateSales;

public class PredictiveEntryPoint extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4387964568864490814L;
	static Logger logger = Logger.getLogger(PredictiveEntryPoint.class);
	static private final String newline = "\n";
	static JFileChooser fileChooser;
	private String[] args = new String[3];
	public static JButton bigDataLocationButton, submitButton;
	public static JCheckBox chkMonth, chkDefects, chkSales, chkFuel;
	public static String bigDataPath = null;
	public static JTextField bigDataLabelTextField;
	JTextArea log;
	public static JComboBox modelComboBox = null;
	static String[] mystring = { "Aria", "Hispano", "Ace", "Divo", "Novus",
			"Ultra", "Manza", "Indigo" };
	public static JList jList1 = new JList(mystring);

	public static void main(String[] args) {
		logger.info(MRConstants.PROJECT_NAME + "version ");
		logger.info("Java version is " + MRConstants.javaVersion);
		logger.info("--inside init of PredictiveEntryPoint main Start-- ");
		new PredictiveEntryPoint();
		logger.info("--inside init of PredictiveEntryPoint main End-- ");
	}

	public PredictiveEntryPoint() {

		log = new JTextArea(5, 20);

		JFrame frame = new JFrame("Predictive Analysis of Tata Car Models");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setFont(getFont());
		frame.setForeground(Color.CYAN);

		frame.setBounds(150, 150, 950, 550);

		JPanel mainPanel = new ImagePanel2("/images/hadoop.jpeg");

		mainPanel.setLayout(new GridLayout(5, 0));

		JPanel textOnTopPanel = new JPanel();

		textOnTopPanel.setOpaque(false);
		mainPanel.add(textOnTopPanel);
		mainPanel.setOpaque(false);
		mainPanel.setBackground(Color.black);
		JLabel headingLabel = new JLabel(
				"<html><u><font color='#2A0A0A' size='6' face='Times'>PREDICTIVE ANALYSIS</font></u></html>",
				JLabel.CENTER);
		textOnTopPanel.add(headingLabel);

		JPanel secondPanel = new JPanel();

		secondPanel.setBackground(Color.black);

		secondPanel.setOpaque(false);

		mainPanel.add(secondPanel);

		JLabel bigDataLabel = new JLabel(
				"<html><font color='lightgray' size='4' face='Comic Sans'>BIGDATA INPUT							:</font></html>",
				JLabel.LEFT);

		secondPanel.add(bigDataLabel);

		bigDataLabelTextField = new JTextField(
				"                                                                                ");
		bigDataLabelTextField.setSize(50, 15);

		secondPanel.add(bigDataLabelTextField);

		bigDataLocationButton = new JButton("Choose a File...",

		createImageIcon("/images/Open16.jpeg"));

		bigDataLocationButton.addActionListener(this);

		secondPanel.add(bigDataLocationButton);

		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("<html><font color='#393B0B' size='4'>"
				+ "CHOOSE A MODEL:" + "</font></html>");
		// modelComboBox = new JComboBox (modelOptions);
		comboPanel.add(comboLbl);
		// comboPanel.add(modelComboBox);
		comboPanel.setLayout(new FlowLayout());
		// modelComboBox.setKeySelectionManager(null);
		// modelComboBox.setSelectedIndex(0);

		JScrollPane menuScrollPane = new JScrollPane(jList1);
		Dimension d = jList1.getPreferredSize();
		d.width = 100;
		d.height = 56;
		menuScrollPane.setPreferredSize(d);
		comboPanel.add(menuScrollPane, BorderLayout.CENTER);

		mainPanel.add(comboPanel);

		comboPanel.setBackground(Color.black);

		comboPanel.setOpaque(false);
		comboPanel.add(new JLabel("<html><br><br><br><br><br><br><br></html>"));

		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new FlowLayout());
		checkBoxPanel.setOpaque(false);
		mainPanel.add(checkBoxPanel);

		chkDefects = new JCheckBox(
				"<html><font size='3' color='#3B240B'>Predict Defects</font></html>");
		chkSales = new JCheckBox(
				"<html><font size='3'  color='#3B240B'>Predict Sales</font></html>");
		chkFuel = new JCheckBox(
				"<html><font size='3' color='#3B240B'>Predict Fuel</font></html>");

		chkMonth = new JCheckBox(
				"<html><font size='3' color='#3B240B'>Predict Month</font></html>");

		chkDefects.addActionListener(this);
		chkSales.addActionListener(this);
		chkFuel.addActionListener(this);
		chkMonth.addActionListener(this);

		checkBoxPanel.add(chkDefects);
		checkBoxPanel.add(chkSales);
		checkBoxPanel.add(chkFuel);
		checkBoxPanel.add(chkMonth);
		checkBoxPanel.add(new JLabel(
				"<html><br><br><br><br><br><br><br><br><br><br></html>"));

		JPanel submitPanel = new JPanel();
		submitPanel.setBackground(Color.BLUE);
		submitPanel.setOpaque(false);
		mainPanel.add(submitPanel);

		submitButton = new JButton("Submit");
		submitButton.setEnabled(true);
		submitButton.addActionListener(this);

		submitPanel.add(submitButton);
		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);
		submitPanel.add(new JLabel(
				"<html><br><br><br><br><br><br><br><br></html>"));
		submitPanel.add(new JLabel(
				"<html><br><br><br><br><br><br><br><br></html>"));

	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = PredictiveEntryPoint.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bigDataLocationButton) {

			String filename = File.separator + "tmp";
			JFileChooser fileChooser = new JFileChooser(new File(filename));
			int returnVal = fileChooser
					.showOpenDialog(PredictiveEntryPoint.this);
			// pop up an "Open File" file chooser dialog

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				bigDataLabelTextField.setText(file.getAbsolutePath());
				bigDataPath = file.getAbsolutePath();

				// System.out.println("home/Desktop/New" +
				// fileChooser.getSelectedFile());

				// pop up an "Save File" file chooser dialog

				// This is where a real application would save the file.
				log.append("Saving: " + file.getAbsolutePath() + "." + newline);

				log.append("Saving: done ");
			} else {
				log.append("Save command cancelled by user." + newline);
			}

		}

		else if (e.getSource() == submitButton) {
			try {
				System.out.println("--- DATA INPUT PATH : "
						+ bigDataLabelTextField.getText());
				MRConstants.BIGDATA_INPUT_PATH = bigDataLabelTextField
						.getText();
				// String predictiveModel = (String) modelComboBox
				// .getSelectedItem();

				// This for model combo
				List<String> selectedModelsList = new ArrayList<String>();
				int[] contents = jList1.getSelectedIndices();
				for (int obj : contents) {
					System.out.println(mystring[obj]);
					selectedModelsList.add(mystring[obj]);

				}

				System.out.println("--- selectedModelsList -- : "
						+ selectedModelsList);

				boolean monthCheck = chkMonth.isSelected();
				boolean defectsCheck = chkDefects.isSelected();
				boolean salesCheck = chkSales.isSelected();
				boolean fuelCheck = chkFuel.isSelected();

				if (monthCheck || defectsCheck || salesCheck || fuelCheck) {
					System.out.println("---predictor selected.");

				} else if (!monthCheck && !defectsCheck && !salesCheck
						&& !fuelCheck) {

					JOptionPane.showMessageDialog(null,
							"Please select atleast one prediction");
				}
				if (bigDataPath == null) {
					JOptionPane.showMessageDialog(null,
							"Please choose Data Location");
				}

				args = new String[2];
				PrescriptionGUI.prescriptionListofList = new ArrayList<List<String>>();
				if (monthCheck) {
					// for (String selectedModel : selectedModelsList) {
					// MRConstants.PREDICTIVE_MODEL = selectedModel;
					new PredictiveEntryMonth(selectedModelsList);

					// }
				}
				if (defectsCheck) {
					for (String selectedModel : selectedModelsList) {
						MRConstants.PREDICTIVE_MODEL = selectedModel;
						AggregateDefects.main(null);
						// PrescriptionGUI.start();
					}
				}
				if (salesCheck) {
					for (String selectedModel : selectedModelsList) {
						MRConstants.PREDICTIVE_MODEL = selectedModel;
						AggregateSales.main(null);
						// PrescriptionGUI.start();
					}
				}
				if (fuelCheck) {
					for (String selectedModel : selectedModelsList) {
						MRConstants.PREDICTIVE_MODEL = selectedModel;
						AggregateFuel.main(null);
						// PrescriptionGUI.start();
					}
				}

				PrescriptionGUI.start();

			} catch (Exception e1) {

			}
		}
	}
}

class ImagePanel2 extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image img;

	public ImagePanel2(String img) {

		this(new ImageIcon(PredictiveEntryPoint.class.getResource(img))
				.getImage());
	}

	public ImagePanel2(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		// setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);

	}
}