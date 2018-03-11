import knights.discord.zerotwo.emotes.CustomEmotes;
import knights.discord.zerotwo.service.IModule;

module knights.discord.zerotwo.emotes {
	requires java.base;
	requires JDA;
	requires knights.discord.zerotwo.service;

	exports knights.discord.zerotwo.emotes;
	provides IModule with CustomEmotes;
}