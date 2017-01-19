package Exceptions;

public class Exceptions extends Exception{
	public Exceptions(Throwable e) {
		initCause(e); 
	}

}
