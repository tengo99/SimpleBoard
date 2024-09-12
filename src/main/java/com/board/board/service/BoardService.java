package com.board.board.service;

import com.board.board.dto.BoardDto;
import com.board.board.entity.BoardEntity;
import com.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDto boardDto) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDto);
        boardRepository.save(boardEntity);
    }

    public List<BoardDto> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDto> boardDtoList = boardEntityList
                .stream()
                .map(b -> new BoardDto(b.getId(), b.getBoardWriter(), b.getBoardPass(), b.getBoardTitle()
                        , b.getBoardContents(), b.getBoardHits(), b.getCreatedTime(), b.getUpdatedTime())).toList();
        return boardDtoList;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDto findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDto boardDto = new BoardDto(boardEntity.getId(), boardEntity.getBoardWriter(), boardEntity.getBoardPass()
                    , boardEntity.getBoardTitle(), boardEntity.getBoardContents(), boardEntity.getBoardHits(), boardEntity.getCreatedTime(), boardEntity.getUpdatedTime());
            return boardDto;
        }
        return null;
    }

    @Transactional
    public BoardDto update(BoardDto boardDto) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardDto.getId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            boardEntity.setBoardWriter(boardDto.getBoardWriter());
            boardEntity.setBoardPass(boardDto.getBoardPass());
            boardEntity.setBoardTitle(boardDto.getBoardTitle());
            boardEntity.setBoardContents(boardDto.getBoardContents());
            boardEntity.setBoardHits(boardDto.getBoardHits());

            BoardDto updatedDto = new BoardDto(boardEntity.getId(), boardEntity.getBoardWriter(), boardEntity.getBoardPass()
                    , boardEntity.getBoardTitle(), boardEntity.getBoardContents(), boardEntity.getBoardHits(), boardEntity.getCreatedTime(), boardEntity.getUpdatedTime());
            return updatedDto;
        }
        return null;
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3; //한 페이지에 보여줄 글 갯수

        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));

        Page<BoardDto> boardDtos = boardEntities.map(BoardEntity -> new BoardDto(BoardEntity.getId(), BoardEntity.getBoardWriter(), BoardEntity.getBoardPass(), BoardEntity.getBoardTitle()
                , BoardEntity.getBoardContents(), BoardEntity.getBoardHits(), BoardEntity.getCreatedTime(), BoardEntity.getUpdatedTime()));

        return boardDtos;
    }
}
