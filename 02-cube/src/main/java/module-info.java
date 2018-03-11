import knights.discord.zerotwo.cube.Cube;
import knights.discord.zerotwo.service.IModule;

module knights.discord.zerotwo.cube {
	requires java.base;
	requires JDA;
	requires jsr305;
	requires knights.discord.zerotwo.service;
	
	exports knights.discord.zerotwo.cube;
	provides IModule with Cube;
}