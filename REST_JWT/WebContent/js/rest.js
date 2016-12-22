'use strict';

class RestApi {
  // ..and an (optional) custom class constructor. If one is
  // not supplied, a default constructor is used instead:
  // constructor() { }
	
  constructor(height, width) {
    this.name = 'Polygon';
    this.height = height;
    this.width = width;
  }

  // Simple class instance methods using short-hand method
  // declaration
  login() {
    console.log('loging');
  }

  getProfile() {
    console.log('get Profile');
    return 'get Profile';
  }
 
  
  // We will look at static and subclassed methods shortly
}