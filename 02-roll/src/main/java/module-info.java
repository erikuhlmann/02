import knights.discord.zerotwo.roll.Roll;
import knights.discord.zerotwo.service.IModule;

module knights.discord.zerotwo.roll {
	requires java.base;
	requires JDA;
	requires knights.discord.zerotwo.service;
	
	exports knights.discord.zerotwo.roll;
	provides IModule with Roll;
}