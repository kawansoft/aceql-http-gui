/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kawansoft.app.util;

/**
 *
 * @author Nicolas de Pomereu
 */
public class HrefBuilder {
    
    private String link = null;
    private String text = null;
    
    
    public HrefBuilder(String link, String text) {
	super();
	this.link = link;
	this.text = text;
    }
    
    public String getHrefLine() {
	return "<a href=\"" + link + "\">" + text + "</a>"; 
    }
    
    
}
