package cn.rongcloud.mic.room.service;

import cn.rongcloud.common.utils.DateTimeUtils;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.room.constant.RoomKeysCons;
import cn.rongcloud.mic.room.dao.RoomMusicDao;
import cn.rongcloud.mic.room.enums.ChrmMusicType;
import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.model.TRoomMusic;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicAdd;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicDelete;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicList;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicMove;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RoomMusicServiceImpl implements RoomMusicService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomMusicDao roomMusicDao;

    @Autowired
    FileService fileService;

    @Value("${rongrtc.filepath}")
    private String filepath;

    @Autowired
    RedisUtils redisUtils;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public RestResult musicMove(ReqRoomMusicMove data, JwtUser jwtUser) {
        //检查房间是否存在
        TRoom room = roomService.getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        TRoomMusic one = roomMusicDao.getOne(data.getToId());
        Integer updateSort = one.getSort() + 1;
        roomMusicDao.updateRoomMusicBySort(data.getRoomId(), one.getSort());
        roomMusicDao.updateRoomMusic(data.getFromId(), updateSort);
        return RestResult.success();
    }

    @Override
    public RestResult musicPlay(ReqRoomMusicDelete data) {
        if(data.getId()==null){
            redisUtils.del(String.format(RoomKeysCons.REDIS_ROOM_MUSIC_PLAY,data.getRoomId()));
            return RestResult.success();
        }
        TRoomMusic roomMusic = roomMusicDao.findTroomMusicById(data.getId());
        if(roomMusic==null){
            return RestResult.generic(RestResultCode.ERR_ROOM_MUSIC_NOT_EXIST);
        }
        log.info("roomMusic:{}", GsonUtil.toJson(roomMusic));
        //redisTemplate.opsForValue().set(String.format(RoomKeysCons.REDIS_ROOM_MUSIC_PLAY,data.getRoomId()),roomMusic,RoomKeysCons.REDIS_ROOM_MUSIC_PLAY_TIME, TimeUnit.SECONDS);
        redisUtils.set(String.format(RoomKeysCons.REDIS_ROOM_MUSIC_PLAY,data.getRoomId()),roomMusic,RoomKeysCons.REDIS_ROOM_MUSIC_PLAY_TIME);
        return RestResult.success();
    }

    @Override
    public RestResult<TRoomMusic> musicPlayGet(String roomId) {
        TRoomMusic roomMusic = (TRoomMusic)redisUtils.get(String.format(RoomKeysCons.REDIS_ROOM_MUSIC_PLAY, roomId));
        return RestResult.success(roomMusic);
    }

    @Override
    public RestResult<List<TRoomMusic>> musicList(ReqRoomMusicList data) {
        TRoom room = roomService.getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        List<TRoomMusic> list;
        if (data.getType() == ChrmMusicType.OFFICIAL.getValue()) {
            list = roomMusicDao.findTRoomMusicByTypeEqualsOrderBySortAsc(ChrmMusicType.OFFICIAL.getValue());
        } else {
            //应该显示的是 非官方音乐的所有音乐
            list = roomMusicDao.findTRoomMusicByTypeGreaterThanAndRoomIdEqualsOrderBySortAsc(ChrmMusicType.OFFICIAL.getValue(), data.getRoomId());
        }

        return RestResult.success(list);

    }

    @Override
    public RestResult addMusic(ReqRoomMusicAdd data, JwtUser jwtUser) {
        TRoom room = roomService.getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        TRoomMusic music = new TRoomMusic();
        Date date = DateTimeUtils.currentUTC();
        music.setCreateDt(date);
        music.setUpdateDt(date);
        music.setName(data.getName());
        music.setRoomId(data.getRoomId());
        music.setAuthor(data.getAuthor());
        music.setUrl(data.getUrl());
        music.setType(data.getType());
        if(StringUtils.isNotBlank(data.getBackgroundUrl())){
            music.setBackgroundUrl(data.getBackgroundUrl());
        }
        if(StringUtils.isNotBlank(data.getThirdMusicId())){
            List<TRoomMusic> roomMusic= roomMusicDao.findTRoomMusicByThirdMusicIdEqualsAndRoomIdEquals(data.getThirdMusicId(),data.getRoomId());
            if(!CollectionUtils.isEmpty(roomMusic)){
                return RestResult.generic(RestResultCode.ERR_ROOM_MIC_NOT_ALLOWED_LOCK);
            }
            music.setThirdMusicId(data.getThirdMusicId());
        }
        if (data.getSize() != null) {
            music.setSize(String.format("%.3f", ((float) data.getSize() / 1024)));
        }
        //获取sort最大的数据+1
        Integer maxSort = roomMusicDao.getMaxSort(data.getRoomId());
        if (maxSort == null) {
            maxSort = 0;
        }
        music.setSort(maxSort + 1);
        //end
        int size = 0;
        if (data.getType() == ChrmMusicType.CUSTOM.getValue()||ChrmMusicType.THIRD_MUSIC.getValue()==data.getType().intValue()) {
            if (music.getSize() == null) {
                try {
                    size = fileService.getFileSize(filepath + jwtUser.getUserId() + "/" + music.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                double f = size / 1024 / 1024;
                music.setSize(String.format("%.3f", f));
            }
        } else {//官方需要从库中获取
            TRoomMusic musicInfo = roomMusicDao.findTRoomMusicByTypeEqualsAndNameEquals(ChrmMusicType.OFFICIAL.getValue(), data.getName());
            music.setSize(musicInfo.getSize());
            music.setUrl(musicInfo.getUrl());
        }
        roomMusicDao.save(music);
        return RestResult.success();
    }

    @Override
    public RestResult musicDelete(ReqRoomMusicDelete data) {

        TRoom room = roomService.getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        roomMusicDao.deleteTRoomMusicByIdEquals(data.getId());
        return RestResult.success();
    }

}
