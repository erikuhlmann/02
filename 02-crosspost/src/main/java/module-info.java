import knights.discord.zerotwo.service.IModule;
import knights.discord.zerotwo.xpost.Crosspost;

module knights.discord.zerotwo.xpost {
	requires knights.discord.zerotwo.service;
	requires JDA;
	requires java.base;
	
	exports knights.discord.zerotwo.xpost;
	provides IModule with Crosspost;
}