package com.mind.predictive.poc.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mind.predictive.poc.mr.util.MRConstants;
import com.mind.predictive.poc.predictors.AggregateMonth;

public class PredictiveEntryMonth {

	public PredictiveEntryMonth(final List<String> selectedModelList) {
		Thread thread = new Thread();
		JFrame guiFrame = new JFrame();

		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Month Prediction on the basis of Year and Month");
		guiFrame.setSize(300, 250);

		// This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		// Options for the JComboBox
		String[] yearOptions = { "2003", "2004", "2005", "2006", "2007",
				"2008", "2009", "2010", "2011", "2012", "2013" };

		// Options for the JList
		String[] countryOptions = { "CHINA", "BELGIUM", "UK", "US", "CANADA",
				"INDIA", "GERMANY", "ITALY", "RUSSIA", "POLAND" };

		// The first JPanel contains a JLabel and JCombobox
		final JPanel comboPanel = new JPanel();
		JLabel comboLbl = new JLabel("<html><font color='black' size='4'>"
				+ "CHOOSE A YEAR:" + "</font></html>");
		final JComboBox yearComboBox = new JComboBox(yearOptions);

		comboPanel.add(comboLbl);
		comboPanel.add(yearComboBox);
		comboPanel.add(new JLabel("<html><br><br><br><br><br><br><br></html>"));

		// Create the second JPanel. Add a JLabel and JList and
		// make use the JPanel is not visible.

		final JPanel comboPanel2 = new JPanel();
		JLabel comboLbl1 = new JLabel("<html><font color='black' size='4'>"
				+ "CHOOSE A COUNTRY:" + "</font></html>");

		final JComboBox countryComboBox = new JComboBox(countryOptions);

		comboPanel2.add(comboLbl1);
		comboPanel2.add(countryComboBox);

		final JButton analysisButton = new JButton("CLICK FOR ANALYSIS");

		// The ActionListener class is used to handle the
		// event that happens when the user clicks the button.
		// As there is not a lot that needs to happen we can
		// define an anonymous inner class to make the code simpler.
		analysisButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// When the fruit of veg button is pressed
				// the setVisible value of the listPanel and
				// comboPanel is switched from true to
				// value or vice versa.

				try {

					{
						String predictiveCountry = (String) countryComboBox
								.getSelectedItem();
						String predictiveYear = (String) yearComboBox
								.getSelectedItem();

						MRConstants.PREDICTIVE_YEAR = predictiveYear;
						MRConstants.PREDICTIVE_COUNTRY = predictiveCountry;

					}
				} catch (Exception e) {
					e.printStackTrace();

				}
				try {
					for (String selectedModel : selectedModelList) {
						MRConstants.PREDICTIVE_MODEL = selectedModel;
						AggregateMonth.main(null);

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				comboPanel2.setVisible(!comboPanel2.isVisible());
				comboPanel.setVisible(!comboPanel.isVisible());

				// }
			}
		});

		// The JFrame uses the BorderLayout layout manager.
		// Put the two JPanels and JButton in different areas.
		guiFrame.add(comboPanel, BorderLayout.NORTH);
		guiFrame.add(comboPanel2, BorderLayout.CENTER);
		guiFrame.add(analysisButton, BorderLayout.SOUTH);

		// make sure the JFrame is visible
		guiFrame.setVisible(true);

		/*
		 * try { testThread.start(); testThread.wait(); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 */
	}

	// TODO Auto-generated method stub

}