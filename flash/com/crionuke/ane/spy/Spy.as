package com.crionuke.ane.spy {
	import flash.external.ExtensionContext;
	
	/**
	 * ...
	 * @author crionuke
	 */
	public class Spy {
		
		private static var context: ExtensionContext;
		
		public static function init(): void {
			if (!context)
				context = ExtensionContext.createExtensionContext("com.crionuke.ane.spy", null);
		}
		
		public static function isSupported(): Boolean {
			return true;
		}
		
		public static function auth(): int {
			if (context)
				return int(context.call("auth"))
			else
				throw new Error("Context doesn't exist");
		}
		
		public static function start(Interval: int, Url: String, Token: String, NTitle: String, NText: String): int {
			if (context)
				return int(context.call("start", Interval, Url, Token, NTitle, NText))
			else
				throw new Error("Context doesn't exist");
		}
		
		public static function check(): int {
			if (context)
				return int(context.call("check"))
			else
				throw new Error("Context doesn't exist");
		}
		
		public static function stop(): int {
			if (context)
				return int(context.call("stop"))
			else
				throw new Error("Context doesn't exist");
		}
		
	}

}