package com.community.cache;

import com.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get() {
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("swift", "shell", "haskell", "c++", "c-sharp", "java", "rust", "typescript", "perl", "c", "js", "php", "html", "golang","css","node"));
        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring", "django", "flask", "struts", "koa", "yli"));
        tagDTOS.add(program);
        tagDTOS.add(framework);
        return tagDTOS;
    }

    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invaild = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invaild;
    }
}
