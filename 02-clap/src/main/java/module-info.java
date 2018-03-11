import knights.discord.zerotwo.clap.Clap;
import knights.discord.zerotwo.service.IModule;

module knights.discord.zerotwo.clap {
	requires java.base;
	requires emoji.java;
	requires JDA;
	requires knights.discord.zerotwo.service;
	
	exports knights.discord.zerotwo.clap;
	provides IModule with Clap;
}