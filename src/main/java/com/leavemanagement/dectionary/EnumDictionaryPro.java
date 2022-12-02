package com.leavemanagement.dectionary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnumDictionaryPro {
	private String key;
	private String value;

	public EnumDictionaryPro(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public EnumDictionaryPro(String key) {
		super();
		this.key = key;
	}

}
