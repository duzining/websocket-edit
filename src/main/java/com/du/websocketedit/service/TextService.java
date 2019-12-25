package com.du.websocketedit.service;

import com.du.websocketedit.entity.Text;

import java.util.List;
import java.util.Map;

public interface TextService {

    List<Map<String, Object>> findAll();

    int addText(Text text);
}
