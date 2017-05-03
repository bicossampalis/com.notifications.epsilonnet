package com.notifications.epsilonnet;   

interface BackgroundServiceListener {     
	void handleUpdate(); 
	String getUniqueID();
} 
