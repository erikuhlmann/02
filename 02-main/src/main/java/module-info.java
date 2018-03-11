module knights.discord.zerotwo {
    // Import automatic modules for pre java 9 libs.
    // These are named according to the maven dependency jar names.
    // See algorithm:
    // https://docs.oracle.com/javase/9/docs/api/java/lang/module/ModuleFinder.html#of-java.nio.file.Path...-
    requires logback.classic;
    requires logback.core;
    requires slf4j.api;
    requires JDA;
    requires jsr305;
    requires commons.collections4;
    requires json;
    requires trove4j;
    requires jna;
    requires nv.websocket.client;
    requires okhttp;
    requires okio;
    
    // Regular modules
    requires java.base;
    
    requires knights.discord.zerotwo.service;
    uses knights.discord.zerotwo.service.IModule;
}