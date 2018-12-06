//var BackgroundServiceFactory = function () {
	var exec = require("cordova/exec");
	var BackgroundService = function () {
		var ServiceName = '';
		this.getServiceName = function() {
			return ServiceName;
		};
		this.setServiceName = function(serviceName) {
			ServiceName = serviceName;
		};
	};

	BackgroundService.prototype.setServiceName = function(serviceName) { 
		this.getServiceName(serviceName);
	};
	/**
	  * Starts the Service
	  * 
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.startService = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'startService',      
						[this.getServiceName()]);
	};

	/**
	  * Stops the Service
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.stopService = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'stopService',      
						[this.getServiceName()]);
	};

	/**
	  * Enables the Service Timer
	  *
	  * @param milliseconds The milliseconds used for the timer
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.enableTimer = function(milliseconds, successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'enableTimer',      
						[this.getServiceName(), milliseconds]);
	};

	/**
	  * Disabled the Service Timer
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.disableTimer = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'disableTimer',      
						[this.getServiceName()]);
	};

	/**
	  * Sets the configuration for the service
	  *
	  * @param configuration JSONObject to be sent to the service
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.setConfiguration = function(configuration, successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'setConfiguration',      
						[this.getServiceName(), configuration]);
	};

	/**
	  * Registers the service for Boot Start
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.registerForBootStart = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'registerForBootStart',      
						[this.getServiceName()]);
	};

	/**
	  * Deregisters the service for Boot Start
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.deregisterForBootStart = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'deregisterForBootStart',      
						[this.getServiceName()]);
	};

	/**
	  * Get the current status of the service.	
	  * 
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.isRegisteredForBootStart = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'isRegisteredForBootStart',      
						[this.getServiceName()]);
	};


	/**
	  * Returns the status of the service
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.getStatus = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'getStatus',      
						[this.getServiceName()]);
	};

	/**
	  * Returns the doWork once
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.runOnce = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'runOnce',      
						[this.getServiceName()]);
	};

	/**
	  * Registers for doWork() updates
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.registerForUpdates = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'registerForUpdates',      
						[this.getServiceName()]);
	};

	/**
	  * Deregisters for doWork() updates
	  *
	  * @param successCallback The callback which will be called if the method is successful
	  * @param failureCallback The callback which will be called if the method encounters an error
	  */
	BackgroundService.prototype.deregisterForUpdates = function(successCallback, failureCallback) { 
		return exec(	successCallback,      
						failureCallback,      
						'BackgroundServicePlugin',      
						'deregisterForUpdates',      
						[this.getServiceName()]);
	};

	var backgroundService = new BackgroundService();
	module.exports = backgroundService;
//}; 
