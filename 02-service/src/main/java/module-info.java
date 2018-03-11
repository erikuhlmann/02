module knights.discord.zerotwo.service {
	// Import automatic modules for pre java 9 libs.
	// These are named according to the maven dependency jar names.
	// See algorithm:
	// https://docs.oracle.com/javase/9/docs/api/java/lang/module/ModuleFinder.html#of-java.nio.file.Path...-
	requires JDA;

	// Regular modules
	requires java.base;

	exports knights.discord.zerotwo.service;
}