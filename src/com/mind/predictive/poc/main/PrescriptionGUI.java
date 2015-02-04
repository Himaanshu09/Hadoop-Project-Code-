package com.mind.predictive.poc.main;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class PrescriptionGUI {
	public static List<List<String>> prescriptionListofList;

	public static void start() {
		JFrame frame = new JFrame();

		JLabel title = new JLabel(
				"<html><u><font color='#837201' size='6'>PRESCRIPTION SUMMARY</font></u></html>",
				JLabel.CENTER);

		frame.getContentPane().add(BorderLayout.NORTH, title);

		int x = 10;
		for (List<String> mainList : prescriptionListofList) {
			for (String precripStr : mainList) {
				JLabel jLabel = new JLabel();
				jLabel.setBounds(0, x, 800, 100);
				x = x + 10;
				jLabel.setText(precripStr);
				frame.add(jLabel);
			}
		}

		frame.setSize(600, 800);
		frame.setVisible(true);
	}
}