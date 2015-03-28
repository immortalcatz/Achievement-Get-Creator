package mc.Mitchellbrine.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mitchellbrine on 2015.
 */
public class Main implements Runnable{

	private static final ArrayList<JSONAchievement> achievements = new ArrayList<JSONAchievement>();

	public static void main(String[] args) {
		Main main = new Main();
		SwingUtilities.invokeLater(main);
	}

	@Override
	public void run() {
		openScreen();
	}

	public void openScreen() {
		final JFrame frame = new JFrame("Achievement Get Creator");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(350,600);
		frame.setResizable(false);

		frame.getContentPane().setBackground(new Color(0,100,0));

		frame.setLayout(new GroupLayout(frame.getContentPane()));

		insertComponents(frame);

		frame.setVisible(true);

	}

	public static JSONAchievement getAchievement(String name) {
		JSONAchievement ach = null;
		for (JSONAchievement achievement : achievements) {
			if (name.equalsIgnoreCase(achievement.id)) ach = achievement;
		}
		return ach;
	}

	@SuppressWarnings("unchecked")
	public void insertComponents(final JFrame frame) {
		String labelString = "Welcome to the Achievement Get achievement creator";
		JLabel label = new JLabel(labelString);
		label.setBounds(5, 0, 350, 20);
		frame.add(label);

		String[] array = getAchNames();

		final JComboBox achBox = new JComboBox(array);
		achBox.setBounds(0,21,350,30);

		frame.add(achBox);

		int yLevel = 52;

		final JTextField id = new JTextField("Id:");
		id.setToolTipText("This value has no default value and must be changed");

		id.setBounds(0,50,350,30);

		yLevel += 30;

		final JTextField name = new JTextField("Name:");
		name.setToolTipText("This value has no default value and must be changed");

		name.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextArea desc = new JTextArea("Description:");
		desc.setToolTipText("This value has no default value and must be changed");

		desc.setBounds(0,yLevel,350,60);

		yLevel += 62;

		final JTextField parent = new JTextField("Parent:");
		parent.setToolTipText("This value has no default value. (CAN BE NULL)");

		parent.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField stat = new JTextField("Stat Name:");
		stat.setToolTipText("This value has no default value and must be changed");

		stat.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField count = new JTextField("Count:");
		count.setToolTipText("This value has no default value and must be changed (MUST BE INTEGER)");

		count.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField xPos = new JTextField("X Position:");
		xPos.setToolTipText("This value has no default value and must be changed (MUST BE INTEGER)");

		xPos.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField yPos = new JTextField("Y Position:");
		yPos.setToolTipText("This value has no default value and must be changed (MUST BE INTEGER)");

		yPos.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField isSpecial = new JTextField("Is it Special:");
		isSpecial.setToolTipText("This value has no default value and must be changed (MUST BE BOOLEAN)");

		isSpecial.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField item = new JTextField("minecraft:apple");

		item.setBounds(0,yLevel,350,30);

		yLevel += 32;

		final JTextField color = new JTextField("gray");

		color.setBounds(0,yLevel,350,30);

		yLevel += 40;

		frame.add(id);
		frame.add(name);
		frame.add(desc);
		frame.add(parent);
		frame.add(stat);
		frame.add(count);
		frame.add(xPos);
		frame.add(yPos);
		frame.add(isSpecial);
		frame.add(item);
		frame.add(color);

		final JButton button = new JButton("Add");
		button.setBounds(10,yLevel,330,40);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				achievements.add(new JSONAchievement(id.getText(),name.getText(),desc.getText(),parent.getText(),stat.getText(),item.getText(),color.getText(),Boolean.parseBoolean(isSpecial.getText()),Integer.parseInt(count.getText()),Integer.parseInt(xPos.getText()),Integer.parseInt(yPos.getText())));
				achBox.setModel(new DefaultComboBoxModel<String>(getAchNames()));
			}
		});

		yLevel += 42;

		final JButton finalize = new JButton("Generate JSON");

		finalize.setBounds(10,yLevel,330,40);

		finalize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder builder = new StringBuilder();
				builder.append("[");
				for (JSONAchievement achievement : achievements) {
					builder.append("{\n");
					builder.append("\t\"id\": \"" + achievement.id + "\",\n");
					builder.append("\t\"name\": \"" + achievement.name + "\",\n");
					builder.append("\t\"desc\": \"" + achievement.desc + "\",\n");
					String parentString = !achievement.parent.equalsIgnoreCase("null") ? "\"" + achievement.parent + "\"" : "null";
					builder.append("\t\"parent\": " + parentString + ",\n");
					builder.append("\t\"stat\": \"" + achievement.stat + "\",\n");
					if (!achievement.item.equalsIgnoreCase("minecraft:apple")) {
						builder.append("\t\"item\": \"" + achievement.item + "\",\n");
					}
					if (!achievement.color.equalsIgnoreCase("gray")) {
						builder.append("\t\"color\": \"" + achievement.color + "\",\n");
					}
					builder.append("\t\"count\": " + achievement.count + ",\n");
					builder.append("\t\"xPos\": " + achievement.xPos + ",\n");
					builder.append("\t\"yPos\": " + achievement.yPos + ",\n");
					if (achievement.isSpecial) {
						builder.append("\t\"special\": true,\n");
					}
					builder.append("}");
					if (achievements.indexOf(achievement) < (achievements.size() - 1)) {
						builder.append(",");
						builder.append("\n");
					}
				}
				builder.append("]");

				File output = new File(IOUtils.getDirectory(), Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "." + Calendar.getInstance().get(Calendar.MINUTE) + ".json");

				if (!output.getParentFile().exists()) {
					output.getParentFile().mkdirs();
				}

				try {
					PrintWriter writer = new PrintWriter(output);
					writer.println(builder.toString());
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		});

		frame.add(button);
		frame.add(finalize);

	}

	public static String[] getAchNames() {
		String[] array = new String[achievements.size()];
		for (JSONAchievement ach : achievements) {
			array[achievements.indexOf(ach)] = ach.id;
		}
		return array;
	}

}
