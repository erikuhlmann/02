import knights.discord.zerotwo.desu.Desu;
import knights.discord.zerotwo.service.IModule;

module knights.discord.zerotwo.emotes {
	requires java.base;
	requires JDA;
	requires knights.discord.zerotwo.service;
	
	exports knights.discord.zerotwo.desu;
	provides IModule with Desu;
}