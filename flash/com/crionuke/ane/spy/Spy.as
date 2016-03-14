package com.crionuke.ane.spy {
	import flash.external.ExtensionContext;
	import flash.system.Capabilities;
	
	/**
	 * ...
	 * @author crionuke
	 */
	public class Spy {
		
		public static const SPY_STARTED: String = "started";
		public static const SPY_STOPPED: String = "stopped";
		
		private static const FUNCTION_START: String = "start";
		private static const FUNCTION_CHECK: String = "check";
		private static const FUNCTION_STOP : String = "stop";
		
		private static const INIT_ERROR_TEXT: String = "Call init method firstly"
		
		private static var mContext: ExtensionContext;
		
		private static var mTestMode: Boolean;
		private static var mLastState: String;
		
		public static function init(StartStateForTestMode: String = SPY_STOPPED): void {
			mLastState = StartStateForTestMode;
			
			if (!mContext) {
				mContext = ExtensionContext.createExtensionContext("com.crionuke.ane.spy", null);
			}
			
			var platform: String = Capabilities.version.substr(0, 3);
			
			if (/(WIN|MAC|LNX)/.exec(platform) != null) {
				mTestMode = true;
			} else {
				mTestMode = false;
			}
		}
		
		public static function get isSupported(): Boolean {
			return true;
		}
		
		public static function get testMode(): Boolean {
			return mTestMode;
		}
		
		public static function start(Interval: int, Url: String, Token: String, NTitle: String, NText: String): String {
			if (mContext) {
				if (mTestMode) {
					return SPY_STARTED;
				} else {
					return String(mContext.call(FUNCTION_START, Interval, Url, Token, NTitle, NText));
				}
			} else {
				throw new Error(INIT_ERROR_TEXT);
			}
		}
		
		public static function check(): String {
			if (mContext) {
				if (mTestMode) {
					return mLastState;
				} else {
					return String(mContext.call(FUNCTION_CHECK));
				}
			} else {
				throw new Error(INIT_ERROR_TEXT);
			}
		}
		
		public static function stop(): String {
			if (mContext) {
				if (mTestMode) {
					mLastState = SPY_STOPPED;
					return mLastState;
				} else {
					return String(mContext.call(FUNCTION_STOP));
				}
				
			} else {
				throw new Error(INIT_ERROR_TEXT);
			}
		}
		
	}

}