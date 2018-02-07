package com.ecollopy.ws;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.ecollopy.ws.utils.RandomHelper;
import com.ecollopy.ws.utils.Triple;
import com.ecollopy.ws.utils.WordProps;
import com.ecollopy.ws.utils.WordProps.Alignment;
import com.ecollopy.ws.utils.WordProps.Order;

public class CGen {
	
	protected static Logger logger =  Logger.getLogger(CGen.class);
	
	/*
I. First input is a list of xoräs 2. Second input a grid parameter S for 
aa grid 3. Error if grid param is less :han length longest 'A'crci 
1. Sort words into descending order 2. rar.do.•nly place langesz word 
at (x, y, where X <= ength (w) if c: y : w) For 
attempt ' match' on words, try •_r,g most recer•t 
the next xcrd: 
_ii) randomly place it must fit current 
	*/
	
	private char[][] _grid;; 
	private int _size;
	private HashMap<String,Triple<Integer, Integer, WordProps>> _wordLocation;
	
	private static final Random RND = new Random();
	
	public CGen(int gridSize)
	{
		
	
		_size = gridSize;
		_grid = new char[_size][_size];
		_wordLocation = new HashMap<String,Triple<Integer,Integer, WordProps>>();

	}
	
	public void display()
	{
		for (int y = 0; y < _size ; y++) {
			for (int x = 0; x < _size ; x++) 
			{
				if (_grid[x][y] != '\0')
					System.out.print(_grid[x][y] + " ");
				else
					System.out.print("#" + " ");
			}
		}
	}
	
	public String createString() 
	{
		StringBuilder sb1 = new StringBuilder();
		for (int y = 0; y < _size ; y++) {
			for (int x = 0; x < _size ; x++) 
			{
				if (_grid[x][y] != '\0')
					sb1.append(_grid[x][y] + " ");
				else
					sb1.append("#" + " ");
			}
		}
		
		sb1.append("\n");
		
		return sb1.toString();
	}
	
	
	public static void main(String[] args)
	{
		JFrame jFrame = new JFrame();
		Dimension d = new Dimension();
		d.setSize(450,250);
		jFrame.setPreferredSize(d);
		jFrame.pack();
		
		InputDialog inD = new InputDialog(jFrame,new CGen(12));
		inD.setPreferredSize(d);
		inD.pack();
		inD.setVisible(true);
		
	}
	
	
	public void createGrid(ArrayList<String> words, String title, int gridSize)
	{
		Collections.sort(words, new Comparator<String>() {
			public int compare(String o1 , String o2)
			{
				if (o1.length() < o2.length())
				{
					return 1;
				}
				else if (o1.length() > o2.length())
				{
					return -1;
				}
				else
					return 0;
			}
		});
		
		CGen cg1 = new CGen(gridSize);
		
		for (String w : words) {
			if (!cg1.overlayWord(w))
				cg1.insertWord(1,w);
		}
		
		cg1.insertIntoPDF(words,title,gridSize);
	}
	
	
	public void insert(String word)
	{
		WordProps wp = new WordProps(_size);
		
		placeWord(word,wp);
	}
	
	public boolean canInsertWord(String word, int x, int y, WordProps wp)
	{
		if (wp.getAlignment().equals(Alignment.HORIZONTAL))
		{
			if (x-1 >= 0 && _grid[x-1][y] != '\0')
				return false;
			if (x + word.length() < _size && _grid[x+word.length()][y] != '\0')
				return false;
			
		}
		else if (wp.getAlignment().equals(Alignment.VERTICAL))
		{
			if (y-1 >= 0 && _grid[x][y-1] != '\0')
				return false;
			if (y + word.length() < _size && _grid[x][y+word.length()] != '\0')
				return false;
		}
		else if (wp.getAlignment().equals(Alignment.DIAGONAL))
		{
			if (x-1 >= 0 && _grid[x-1][y] != '\0')
				return false;
			if (x + word.length() < _size && _grid[x+word.length()][y] != '\0')
				return false;
			if (y-1 >= 0 && _grid[x][y-1] != '\0')
				return false;
			if (y + word.length() < _size && _grid[x][y+word.length()] != '\0')
				return false;
			if ((x + word.length() < _size) && (y + word.length() < _size) && _grid[x+word.length()][y+word.length()] != '\0')
				return false;			
		}
		
		for (int charPos = 0; charPos < word.length() ; charPos++ )
		{
			char targetChar = _grid[x][y];
			if (targetChar == '\0' || matchOnLetter(word,wp,charPos,targetChar))
			{
				if (wp.getAlignment().equals(Alignment.HORIZONTAL))
					x++;
				else if (wp.getAlignment().equals(Alignment.VERTICAL))
					y++;
				else
				{
					x++;y++;
				}
			}
			else
				return false;
		}
		
		return true;
			
	}
	
	
	
	public boolean matchOnLetter(String word, WordProps wp, int charPos, char targetLetter)
	{
		Order o = wp.getOrder();
		if (o.equals(Order.FORWARDS))
		{
			return targetLetter == word.charAt(charPos);
		}
		else
		{
			return targetLetter == word.charAt((word.length()-1) - charPos);
		}
	}
	
	
	public void insertWord(int BoardNum, String word)
	{
		WordProps wp = new WordProps(_size);
		boolean inserted = false;
		int attempts = 0;
		
		while (!inserted)
		{
			attempts++;
			
			wp.setAlignment(RandomHelper.setAlignment());
			wp.setOrder(RandomHelper.setOrder());
			
			int x = RND.nextInt(wp.getXLimit(word.length()));
			int y = RND.nextInt(wp.getYLimit(word.length()));
			
			if (canInsertWord(word, x, y, wp)) {
				logger.info(" canInsertWord : " + word);
				insertWordinGrid(x,y,word,wp);
				inserted = true;
			}
			
			if (attempts % 10000 == 0)
			{
				logger.info(BoardNum + " Attempt: " + attempts + " Word: " + word + " X: " + x + " Y: " + y + " " + wp.getAlignment().toString() +
						" " + wp.getOrder().toString());
			//	display();
			}
			
			if (attempts > 100000000)
			{
				logger.info(" Could not create board ");
				System.exit(-1);
			}
		}
		
	}
	
	
	public boolean overlayWord(String word)
	{
		for (String w : _wordLocation.keySet())
		{
			logger.trace(" OVERLAy: " + w);
			
			Triple<Integer, Integer, WordProps> p1 = _wordLocation.get(w);
			int x = p1.getFirst();
			int y = p1.getSecond();
			WordProps wp = p1.getThird();
			
			if (wp.getAlignment().equals(Alignment.HORIZONTAL))
			{
				wp.setAlignment(Alignment.VERTICAL);
				if (slideInsertWord(word,x+1,y,wp,w))
					return true;
			}
			else if (wp.getAlignment().equals(Alignment.VERTICAL))
			{
				wp.setAlignment(Alignment.HORIZONTAL);
				if (slideInsertWord(word,x,y+1,wp,w))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean slideInsertWord(String w, int x , int y, WordProps targetProps, String sourceWord)
	{
		boolean inserted = false;
		
		int sourceWordLength = sourceWord.length() - 1;
		
		int cellsChecked = 0;
		
		while (!inserted && cellsChecked < sourceWordLength)
		{
			
			if (w.indexOf(_grid[x][y]) != -1)
			{
				if (placeOverlayWord(w,x,y,targetProps))
					inserted = true;
			}
			else
			{
				if (targetProps.getAlignment().equals(Alignment.HORIZONTAL))
				{
					y++;
				}
				else if (targetProps.getAlignment().equals(Alignment.VERTICAL))
				{
					x++;
				}
			}
			
			cellsChecked++;
		}
		
		return inserted;
	}
	
	
	public boolean placeOverlayWord(String w, int x, int y, WordProps wp)
	{
		char c = _grid[x][y];
		
		int cPos = w.indexOf(c);
		
		if (wp.getAlignment().equals(Alignment.HORIZONTAL))
		{
			int startPos = x-cPos;
			if (startPos < 0 || startPos+w.length() > _size)
				return false;
			else
			{
				if (canInsertWord(w, startPos, y, wp))
				{
					insertWordinGrid(startPos, y , w, wp);
					return true;
					
				}
				else
					return false;
			}
		}
		else if (wp.getAlignment().equals(Alignment.VERTICAL))
		{
			int startPos = y-cPos;
			if (startPos < 0 || startPos+w.length() > _size)
				return false;
			else
			{
				if (canInsertWord(w, x, startPos, wp))
				{
					insertWordinGrid(x, startPos , w, wp);
					return true;
					
				}
				else
					return false;
			}
		}
		
		return false;
		
	}
	
	
	int process(Boolean decrement, int charPos)
	{
		if (decrement)
			return --charPos;
		else
			return ++charPos;
	}
	
	
	void placeWord(String word, WordProps wp)
	{
		int x = RND.nextInt(wp.getXLimit(word.length()));
		int y = RND.nextInt(wp.getYLimit(word.length()));
		
		insertWordinGrid(x,y,word,wp);
	}
	
	void insertWordinGrid(int x, int y, String word, WordProps wp)
	{
		int charPos = 0;
		boolean backwards = false;
		if (wp.getOrder() == Order.BACKWARDS)
		{
			charPos = word.length() - 1;
			backwards = true;
			logger.trace(" INSERT BACKWARDS ");
		}
		
		for (int i = 0 ; i < word.length() ; i++ )
		{
			logger.trace(" LOCA x: " + x +  " y: " + y + " characterPosition: " + charPos);
			
			_grid[x][y] = word.charAt(charPos);
			
			if (wp.getAlignment() == Alignment.HORIZONTAL)
				x++;
			else if (wp.getAlignment() == Alignment.VERTICAL)
				y++;
			else if (wp.getAlignment() == Alignment.DIAGONAL)
			{
				x++;y++;
			}
			
			charPos = process(backwards, charPos);
			
		}
		
		
	}
	
	public String getFileLocation()
	{
		String fileName = "WordSearchPuzzle.pdf";
		String userHomeFolder = System.getProperty("user.home");
		
		return userHomeFolder + "/" + fileName;
	}
	
	public void insertIntoPDF(ArrayList<String> words, String title, int size )
	{
		
		
		String targetFileSave = getFileLocation(); // new String("D:/Downloads/monitorTest.pdf");
		PDDocument document = new PDDocument();
		
		try
		{
			document.save(targetFileSave);
			
			PDPage page = new PDPage();
			document.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			
			contentStream.beginText();
			contentStream.setFont(PDType1Font.COURIER, 12);
			contentStream.setLeading(14.5f);
			
			contentStream.newLineAtOffset(250, 750);
			contentStream.showText(title);
			contentStream.newLineAtOffset(-230, -12);
			contentStream.newLine();
			
			if (size < 10)
				contentStream.setFont(PDType1Font.COURIER, 12);
			else if (size >= 10 && size < 13)
				contentStream.setFont(PDType1Font.COURIER, 12);
			else if (size >= 13 && size < 15)
				contentStream.setFont(PDType1Font.COURIER, 10);
			else if (size >= 15 )
				contentStream.setFont(PDType1Font.COURIER, 8);
			
			contentStream.setWordSpacing(6);
			contentStream.setCharacterSpacing(6);
			
			createString(contentStream);
			
			contentStream.setFont(PDType1Font.COURIER, 12);
			contentStream.setLeading(14.5f);
			
			contentStream.newLine();
			
			int limit = 2;
			int lineLimit = 10;
			StringBuilder[] sb = new StringBuilder[lineLimit];
			
			int k = 0;
			int wordCount = 0;
			
			for (String word : words)
			{
				logger.info(" Add " + word + " to release ");
				if (sb[k] == null)
					sb[k] = new StringBuilder();
				
				sb[k].append(word);
				sb[k].append(" ");
				
				wordCount++;
				if (wordCount >= limit)
				{
					logger.info(" move to newline ");
					k++;
					wordCount = 0;
				}
			}
			
			
			for (int i = 0 ; i < lineLimit ; i++ )
			{
				if (sb[i] != null && sb[i].length() > 0)
				{
					contentStream.showText(sb[i].toString());
					contentStream.newLine();
				}
			}
			
			contentStream.endText();
			contentStream.close();
			
			document.save(targetFileSave);
			document.close();
			
		}
		catch (IOException e ) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void createString(PDPageContentStream contentStream) throws IOException
	{
		int i = 0;
		for (int y = 0; y < _size ; y++)
		{
			StringBuilder sb1 = new StringBuilder();
			
			for (int x = 0 ; x < _size ; x++ )
			{
				
				// '\0' is the default char value 
				if (_grid[x][y] != '\0')
				{
					String letter = Character.toString(_grid[x][y]).toUpperCase();
					sb1.append(letter + " ");
				}
				else
				{
					sb1.append(randomLetter() + " ");
				}
			}
			
			contentStream.showText(sb1.toString());
			contentStream.newLineAtOffset(0, -24);
			
			i = i + 12;
		}
	}
	
	
	static void displayJFrame()
	{
		JFrame jframe = new JFrame (" Size example ");
		jframe.setPreferredSize(new Dimension(400,300));
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
		
	}
	
	String randomLetter()
	{
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		char letter = abc.charAt(RND.nextInt(abc.length()));
		return Character.toString(letter);
	}
	
	
	
	
	
}
