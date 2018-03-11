import knights.discord.zerotwo.ping.Ping;
import knights.discord.zerotwo.service.IModule;

module knights.discord.zerotwo.ping {
	requires java.base;
	requires JDA;
	requires knights.discord.zerotwo.service;

	exports knights.discord.zerotwo.ping;
	provides IModule with Ping;
}