package com.ecollopy.ws;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class InputDialog extends JDialog implements PropertyChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	protected static Logger logger = Logger.getLogger(InputDialog.class);
	
	private Collection<String> words = new ArrayList<String>();
	
	private JTextField wordField = new JTextField();
	private JTextField titleField = new JTextField();
	private JTextArea jText = new JTextArea("",5,5);
	private Integer[] gridSizeList = { 8, 10 , 12, 15, 20 };
	private JComboBox<Integer> sizeList = new JComboBox<Integer>(gridSizeList);
	private JButton okButton = new JButton(" Generate Board ");
	
	private CGen _c;
	private JFrame parent;
	
	public InputDialog(JFrame j, CGen c )
	{
		
		super(j, "Enter Search Parameters " , true);
		parent = j;
		_c = c;
		
		createContents();
		
		addPropertyChangeListener(this);
		
		jText.setEditable(false);
		jText.setLineWrap(true);
		jText.setBackground(okButton.getBackground());
		
		okButton.setName(" Generate Board ");
		
		wordField.addKeyListener(new java.awt.event.KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == '\r')
					addWordToCollection(wordField.getText());
			}
		});
	}

	


	private void createContents()
	{
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		JPanel filterPanel = new JPanel(new GridBagLayout());
		filterPanel.setBorder(BorderFactory.createEtchedBorder());
		
		mainPanel.add(filterPanel, BorderLayout.CENTER);
		
		mainPanel.add(createTextPanel(), BorderLayout.CENTER);
		mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
		mainPanel.setVisible(true);
	}
	
	
	private JPanel createButtonPanel()
	{
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.setBorder(BorderFactory.createEtchedBorder());
		
		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		
		buttonPanel.add(okButton);
		okButton.addActionListener(
				(ActionListener)EventHandler.create(ActionListener.class, this, "startSearch" ));
		
		JButton clearWord = new JButton(" Delete Words ");
		clearWord.setName("Clear Word");
		clearWord.addActionListener(
				(ActionListener)EventHandler.create(ActionListener.class, this, "clearWord" ));
		
		JPanel buttonConstrain = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonConstrain.setBorder(BorderFactory.createEtchedBorder());
		buttonConstrain.add( clearWord );
		
		layoutPanel.add(buttonPanel, BorderLayout.LINE_START);
		layoutPanel.add(buttonConstrain, BorderLayout.LINE_END);
		
		return layoutPanel;
		
	}
	
	
	private JPanel createTextPanel()
	{
		JPanel ui = new JPanel(new BorderLayout(2,2));
		
		JPanel layoutPanel = new JPanel(new BorderLayout(2,2));
		
		layoutPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel titlePanel = new JPanel(new BorderLayout(2,2));
		
		JLabel titleLabel = new JLabel();
		titleLabel.setText(" Enter PUzzle Title: " );
		titlePanel.add(titleLabel,BorderLayout.LINE_START);
		titlePanel.add(titleField, BorderLayout.CENTER );
		
		JPanel textPanel = new JPanel(new GridLayout(0,1,10,30));
		textPanel.add(wordField);
		
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,10,30));
		
		JButton addWord = new JButton(" Add Word to Puzzle ");
		addWord.setName("Add Word");
		buttonPanel.add(addWord);
		addWord.addActionListener(
				(ActionListener)EventHandler.create(ActionListener.class, this, "addWord"));
		
		
		sizeList.setSelectedIndex(3);
		sizeList.addActionListener(
				(ActionListener)EventHandler.create(ActionListener.class, this, "sizeList"));
		
		JPanel sizePanel = new JPanel(new GridLayout(0,1,10,30));
		sizePanel.add(sizeList);
		
		layoutPanel.add(titlePanel, BorderLayout.PAGE_START);
		layoutPanel.add(sizePanel, BorderLayout.LINE_START);
		layoutPanel.add(textPanel, BorderLayout.CENTER);
		layoutPanel.add(buttonPanel, BorderLayout.LINE_END);
		
		JPanel viewPanel = new JPanel(new BorderLayout(2,2));
		viewPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel textAreaPanel = new JPanel(new BorderLayout(2,2));
		JScrollPane sp = new JScrollPane( jText );
		
		textAreaPanel.add(sp,BorderLayout.CENTER);
		
		viewPanel.add(textAreaPanel);
		
		ui.add(layoutPanel, BorderLayout.PAGE_START);
		ui.add(viewPanel, BorderLayout.CENTER);
		
		return ui;	
		
	}
	
	
	public void addWord()
	{
		firePropertyChange("addWordToCollection" , null, wordField.getText() );
	}
	
	public void clearWord()
	{
		firePropertyChange("clearWord" , null, wordField.getText() );
	}
	
	public void sizeList()
	{
		firePropertyChange("sizeList", null, (Integer)sizeList.getSelectedItem() );
	}
	
	public void startSearch()
	{
		firePropertyChange("startSearch", null , (Integer)sizeList.getSelectedItem() );
	}
	
	
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals("addWordToCollection")) 
		{
			addWordToCollection((String) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals("clearWord"))
		{
			clearWords((String) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals("startSearch"))
		{
			startSearch((Integer) evt.getNewValue());
		}
		else if (evt.getPropertyName().equals("sizeList"))
		{
			sizeList((Integer) evt.getNewValue());
		}
								
	}


	private void sizeList(Integer gridSize) {
		okButton.setText("Generate Board, size" + gridSize + " X " + gridSize );
		
	}


	private Object startSearch(int gridSize) {
		String title = titleField.getText();
		if (title == null || title.isEmpty())
		{
			title = " Wordsearch PUzzle";
			
		}
		
		ArrayList<String> myWords = new ArrayList<String>(words);
		ArrayList<String> longWords = new ArrayList<String>();
		
		boolean readyToRun = true;
		
		for (String word : words)
		{
			if (word.length() > gridSize)
			{
				longWords.add(word);
				readyToRun = false;
			}
		}
		
		if (readyToRun)
			_c.createGrid(myWords, title, gridSize);
		else
		{
			
			for (String longWord : longWords)
			{
				words.remove(longWord);
			}
			
			JOptionPane.showMessageDialog(null,"Word is too long for grid - please reenter ");
			
			updateTextArea();
			wordField.setText("");
		}
		
		return null;
		
	}


	private void clearWords(String newValue) {
		words.clear();
		updateTextArea();
		
	}
	
	private void updateTextArea()
	{
		jText.setText("");
		
		for (String w : words)
		{
			jText.append(w);
			jText.append("\n");
		}
	}


	private void addWordToCollection(String newValue) {
		if (!words.contains(newValue.toLowerCase()))
			words.add(newValue);
		
		updateTextArea();
		wordField.setText("");
		
	}
	
	
}
