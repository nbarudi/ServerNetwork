package ca.bungo.hardcore.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class HelpUtility {
	
	public TextComponent mainComp;
	public TextComponent infoComp;
	public TextComponent skillPointsComp;
	public TextComponent creditsComp;
	public TextComponent bountiesComp;
	public TextComponent commandsComp;
	public TextComponent craftingComp;
	
	private TextComponent info;
	private TextComponent skillPoints;
	private TextComponent credits;
	private TextComponent bounties;
	private TextComponent commands;
	private TextComponent crafing;
	
	public HelpUtility() {
		setupCategories();
		setupMainComp();
		setupInfoComp();
	}
	
	private void setupCategories() {
		info = new TextComponent("[Information]");
		info.setColor(ChatColor.DARK_PURPLE);
		info.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/help info"));
		info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /help info Command")));
		
		skillPoints = new TextComponent("[Skill Points]");
		skillPoints.setColor(ChatColor.DARK_PURPLE);
		skillPoints.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/help sp"));
		skillPoints.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /help sp Command")));
		
		credits = new TextComponent("[Credits]");
		credits.setColor(ChatColor.DARK_PURPLE);
		credits.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/help credits"));
		credits.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /help credits Command")));
		
		bounties = new TextComponent("[Bounties]");
		bounties.setColor(ChatColor.DARK_PURPLE);
		bounties.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/help bounties"));
		bounties.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /help bounties Command")));
		
		commands = new TextComponent("[Commands]");
		commands.setColor(ChatColor.DARK_PURPLE);
		commands.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/help commands"));
		commands.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /help commands Command")));
		
		crafing = new TextComponent("[Crafting]");
		crafing.setColor(ChatColor.DARK_PURPLE);
		crafing.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/help crafting"));
		crafing.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /help crafting Command")));
	}
	
	private void setupMainComp() {
		mainComp = new TextComponent();
		mainComp.setColor(ChatColor.GRAY);
		mainComp.addExtra("[");
		TextComponent s1 = new TextComponent("Bungo ");
		s1.setColor(ChatColor.AQUA);
		TextComponent s2 = new TextComponent("Networks");
		s2.setColor(ChatColor.GOLD);
		TextComponent s3 = new TextComponent("]\n\n");
		s3.setColor(ChatColor.GRAY);
		mainComp.addExtra(s1);
		mainComp.addExtra(s2);
		mainComp.addExtra(s3);
		s1 = new TextComponent("Welcome to the Help Menu!\n"
				+ "Here you can find some Server Information!\n"
				+ "Click on what you need help with:\n\n");
		s1.setColor(ChatColor.DARK_AQUA);
		mainComp.addExtra(s1);
		s2 = new TextComponent();
		s2.addExtra(info);
		s2.addExtra(" ");
		s2.addExtra(skillPoints);
		s2.addExtra(" ");
		s2.addExtra(credits);
		s2.addExtra("\n");
		s2.addExtra(bounties);
		s2.addExtra(" ");
		s2.addExtra(commands);
		s2.addExtra(" ");
		s2.addExtra(crafing);
		mainComp.addExtra(s2);
	}
	
	private void setupInfoComp() {
		infoComp = new TextComponent();
		
		TextComponent s1;
		TextComponent s2;
		TextComponent s3;
		
		s1 = new TextComponent("[Information]\n");
		s1.setColor(ChatColor.GREEN);
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Simple Introduction / Information about the game!")));
		infoComp.addExtra(s1);
		
		s1 = new TextComponent("Welcome to the Hardcore Server!\n\n");
		s1.setColor(ChatColor.BLUE);
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Created by nbarudi#0001")));
		
		s2 = new TextComponent("The game is very simple! You have 3 Lives! Lose all 3 lives, and\nlose your ability to play! "
				+ "Now that being said, this doesn't\nmean that once you've lost lives its its over for you.\n"
				+ "There are 2 up sides to this fact.\n");
		s2.setColor(ChatColor.YELLOW);
		
		s3 = new TextComponent("1. You can Craft or Buy Lives!\n"
				+ "   Lives can be purchases in the Shop!\n"
				+ "   Lives can also be crafted using some custom items!\n"
				+ "   Search Life in your Recipe Book to see how!\n\n");
		s3.setColor(ChatColor.AQUA);
		
		infoComp.addExtra(s1);
		infoComp.addExtra(s2);
		infoComp.addExtra(s3);
		
		s1 = new TextComponent("2. Worst case you lose all your lives? What Happens Next?\n"
				+ "   Simple! Just wait! If you lose all your lives just wait!\n"
				+ "   You will automatically be revived after a set amount of time!\n"
				+ "   Then you are free to rejoin the server and you can\n"
				+ "   Craft/Buy more lives!");
		s1.setColor(ChatColor.AQUA);
		infoComp.addExtra(s1);
	}

}
