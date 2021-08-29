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
		setupSkillPointComp();
		setupCreditsComp();
		setupBountiesComp();
		setupCraftingComp();
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
				+ "   Craft/Buy more lives!\n\n");
		s1.setColor(ChatColor.AQUA);
		
		s2 = new TextComponent("So what do you have to keep in mind?\n");
		s2.setColor(ChatColor.YELLOW);
		
		s3 = new TextComponent("Every time you lose all your lives your time to revive\nincreases!\n"
				+ "The starting time for a revive is 12 hours.\n"
				+ "Each time you lose all lives, this time increases by 12 hours.\n\n");
		s3.setColor(ChatColor.AQUA);
		
		infoComp.addExtra(s1);
		infoComp.addExtra(s2);
		infoComp.addExtra(s3);
		
		s1 = new TextComponent("Dont want to wait for your time limit? Well we have a solution to\nthat to!\n"
				+ "Take a run to our website to learn more!");
		s1.setColor(ChatColor.YELLOW);
		
		s2 = new TextComponent(" xxxxxxxxxx");
		s2.setColor(ChatColor.LIGHT_PURPLE);
		s2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Open our website!")));
		s2.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://google.ca"));
		
		infoComp.addExtra(s1);
		infoComp.addExtra(s2);
	}
	
	private void setupSkillPointComp() {
		skillPointsComp = new TextComponent();
		
		TextComponent s1;
		TextComponent s2;
		TextComponent s3;
		
		s1 = new TextComponent("[Skill Points]\n\n");
		s1.setColor(ChatColor.GREEN);
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Everything you need to know about Skill Points!")));
		
		s2 = new TextComponent("The Shop!\n\n");
		s2.setColor(ChatColor.RED);
		s2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/shop"));
		s2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /shop command")));
		
		s3 = new TextComponent("Inside of the shop you will find 2 sections!\n"
				+ "One of these sections sells items that cost Skill Points\n"
				+ "   Inside of this shop is where you will be able to purchase\n   lives!\n");
		s3.setColor(ChatColor.AQUA);
		
		skillPointsComp.addExtra(s1);
		skillPointsComp.addExtra(s2);
		skillPointsComp.addExtra(s3);
		
		s1 = new TextComponent("   Lives each cost 3 skill points\n");
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Atleast at the time of writing this")));
		s1.setColor(ChatColor.AQUA);
		
		s2 = new TextComponent("   Purchasing some items will require you to have a level\n   requirement.\n");
		s2.setColor(ChatColor.AQUA);
		s2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Levels are gained from killing Mobs/Players")));
		
		s3 = new TextComponent("   Your level will show in chat or through the Stats Command.\n\n");
		s3.setColor(ChatColor.AQUA);
		s3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /stats command")));
		s3.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/stats"));
		
		skillPointsComp.addExtra(s1);
		skillPointsComp.addExtra(s2);
		skillPointsComp.addExtra(s3);
		
		s1 = new TextComponent("Perks!\n\n");
		s1.setColor(ChatColor.RED);
		s1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/perks"));
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /perks command")));
		
		s2 = new TextComponent("Welcome to the perk system!\n");
		s2.setColor(ChatColor.YELLOW);
		s3 = new TextComponent("Perks are this servers way of rewarding players for grinding\nlevels!\n"
				+ "   Each perk has its own cost and function\n"
				+ "   There are many perks that will keep you leveling for a longn   time!\n"
				+ "   This way you will always have a goal to keep grinding levels!\n"
				+ "Locked Perks\n"
				+ "   Some perks will only be given a codename!\n"
				+ "   As time goes on we will be adding more perks\n"
				+ "   As well we will enable some of the ones we have hidden away!");
		s3.setColor(ChatColor.AQUA);
		
		skillPointsComp.addExtra(s1);
		skillPointsComp.addExtra(s2);
		skillPointsComp.addExtra(s3);
	}
	
	private void setupCreditsComp() {
		creditsComp = new TextComponent();
		
		TextComponent s1;
		TextComponent s2;
		TextComponent s3;
		
		s1 = new TextComponent("[Credits]\n");
		s1.setColor(ChatColor.GREEN);
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Everything you need to know about credits!")));
		
		s2 = new TextComponent("Credits are the second form of in game currency!\n"
				+ "You earn credits at the same rate as experience!\n"
				+ "Credits are used for many different things!\n"
				+ "They function the same as any servers economy allowing you\nto buy and sell from other players.\n");
		s2.setColor(ChatColor.YELLOW);
		
		s3 = new TextComponent("Player Shops\n"
				+ "   You have the ability to run your own shop!\n"
				+ "   You are free to sell any item you want to!\n"
				+ "   From Lives, Enchantment Books, Mob Spawners.\n"
				+ "   Anything you want you are free to sell for what ever cost\n   you see fit.\n\n"
				+ "   Shops are rented in the Community Mall.\n"
				+ "   It'll get harder to find open stalls as time goes on so if you\n   have an idea\n"
				+ "   There is no need to wait!\n\n");
		s3.setColor(ChatColor.AQUA);
		
		creditsComp.addExtra(s1);
		creditsComp.addExtra(s2);
		creditsComp.addExtra(s3);
		
		s1 = new TextComponent("The Shop!\n\n");
		s1.setColor(ChatColor.RED);
		s1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/shop"));
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /shop command")));
		
		s2 = new TextComponent("Inside of the shop you will find 2 sections!\n");
		s2.setColor(ChatColor.YELLOW);
		
		s3 = new TextComponent("One of these sections sells items that cost Credits!\n"
				+ "   Inside of the shop is where you can purchase BindingAgents\n"
				+ "   The main crafting ingredient for the majority of custom\n   items\n"
				+ "   You can also purchase more Land Claim Blocks from the\n   shop");
		s3.setColor(ChatColor.AQUA);
		
		creditsComp.addExtra(s1);
		creditsComp.addExtra(s2);
		creditsComp.addExtra(s3);
	}
	
	private void setupBountiesComp() {
		bountiesComp = new TextComponent();
		
		TextComponent s1;
		TextComponent s2;
		TextComponent s3;
		
		s1 = new TextComponent("[Bounties]\n");
		s1.setColor(ChatColor.GREEN);
		s1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Execute /bounty command")));
		s1.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/bounty"));
		
		s2 = new TextComponent("The server has a bounty system for those who are Player\nKillers!\n"
				+ "Every time you kill a player, your bounty is constantly\nincreased!\n"
				+ "As your bounty increases players will be given more\ninformation to find you.\n"
				+ "Players can claim the bounty on your head by killing you!\n\n");
		s2.setColor(ChatColor.YELLOW);
		
		s3 = new TextComponent("You're required to be atleast level 5 to view players bounties!\n\n"
				+ "Notes: TEAMS CANNOT CLAIM BOUNTIES OF OTHER TEAMMATES");
		s3.setColor(ChatColor.RED);
		
		bountiesComp.addExtra(s1);
		bountiesComp.addExtra(s2);
		bountiesComp.addExtra(s3);
	}
	
	private void setupCraftingComp() {
		craftingComp = new TextComponent();
		
		TextComponent s1;
		TextComponent s2;
		TextComponent s3;
		
		s1 = new TextComponent("[Crafting]\n");
		s1.setColor(ChatColor.GREEN);
		
		s2 = new TextComponent("Majority of custom items have crafting recipies!\n"
				+ "Although they are not easy to create crafting items tend to be\neasier\n"
				+ "then purchasing them from the Shop.\n\n");
		s2.setColor(ChatColor.YELLOW);
		
		s3 = new TextComponent("Many crafting recipes are located in your recipe book where you can see what you require.");
		s3.setColor(ChatColor.RED);
		
		craftingComp.addExtra(s1);
		craftingComp.addExtra(s2);
		craftingComp.addExtra(s3);
	}

}
