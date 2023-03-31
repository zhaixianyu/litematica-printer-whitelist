package com.zxy.mixin;


import com.zxy.VerifyServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;


@Mixin(MinecraftServer.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
		if(VerifyServer.getVer()==null){
			new VerifyServer().startVerify();
			return;
		}
		VerifyServer.getVer().startVerify();
	}
	@Inject(at = @At("HEAD"),method = "stop")
	public void exit(CallbackInfo ci){
		try {
			VerifyServer.run = false;
			VerifyServer.ssoc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}