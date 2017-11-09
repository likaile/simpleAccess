package cn.lkl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lkl.dao.ImageDao;
import cn.lkl.service.ImageService;
import cn.lkl.vo.JianDanImage;


@Service
public class ImageServiceImpl implements ImageService {
	@Autowired
    private ImageDao ImageMapper;
	@Override
	public JianDanImage getImageById(int id) {
		return ImageMapper.getImageById(id);
	}

}
