package com.example.mytv.adapter.in.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mytv.adapter.in.api.dto.CommentRequest;
import com.example.mytv.application.port.in.CommentUseCase;
import com.example.mytv.application.port.out.LoadUserPort;
import com.example.mytv.application.port.out.UserSessionPort;
import com.example.mytv.domain.comment.Comment;
import com.example.mytv.domain.comment.CommentFixtures;
import com.example.mytv.domain.user.User;
import com.example.mytv.domain.user.UserFixtures;
import com.example.mytv.exception.BadRequestException;
import com.example.mytv.exception.DomainNotFoundException;
import com.example.mytv.exception.ForbiddenRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentApiController.class)
class CommentApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentUseCase commentUseCase;
    @MockBean
    private UserSessionPort userSessionPort;
    @MockBean
    private LoadUserPort loadUserPort;

    private String authKey;
    private User user;

    @BeforeEach
    void setUp() {
        authKey = UUID.randomUUID().toString();
        user = UserFixtures.stub();

        given(userSessionPort.getUserId(anyString())).willReturn(user.getId());
        given(loadUserPort.loadUser(anyString())).willReturn(Optional.of(user));
    }

    @Nested
    @DisplayName("POST /api/v1/comments")
    class CreateComment {
        @Test
        @DisplayName("200 OK, 생성된 id를 반환")
        void testCreateComment() throws Exception {
            // given
            var request = new CommentRequest("channelId", "videoId", "comment");
            given(commentUseCase.createComment(any(), any())).willReturn(CommentFixtures.stub("commentId"));

            // when
            mockMvc
                .perform(
                    post("/api/v1/comments")
                        .header(HeaderAttribute.X_AUTH_KEY, authKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.id").value("commentId")
                );
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/comments")
    class UpdateComment {
        @Test
        @DisplayName("200 OK, 변경된 댓글의 id를 반환")
        void testUpdateCommentThenOk() throws Exception {
            // given
            var commentId = "commentId";
            var request = new CommentRequest("channelId", "videoId", "new comment");
            given(commentUseCase.updateComment(any(), any(), any())).willReturn(CommentFixtures.stub(commentId));

            // when
            mockMvc
                .perform(
                    put("/api/v1/comments/{commentId}", commentId)
                        .header(HeaderAttribute.X_AUTH_KEY, authKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.id").value(commentId)
                );
        }

        @Test
        @DisplayName("401 BadRequest, 댓글 meta 정보가 다를 경우 수정 실패")
        void testGivenInvalidMetaDataWhenUpdateCommentThenBadRequest() throws Exception {
            // given
            var commentId = "commentId";
            var request = new CommentRequest("otherChannelId", "otherVideoId", "new comment");
            given(commentUseCase.updateComment(any(), any(), any()))
                .willThrow(new BadRequestException("Request metadata is invalid."));

            // when
            mockMvc
                .perform(
                    put("/api/v1/comments/{commentId}", commentId)
                        .header(HeaderAttribute.X_AUTH_KEY, authKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.type").value("badRequest")
                );
        }

        @Test
        @DisplayName("403 Forbidden, 댓글 작성자와 API 호출자가 다름 수정 실패")
        void testGivenOtherAuthorWhenUpdateCommentThenForbidden() throws Exception {
            // given
            var commentId = "commentId";
            var request = new CommentRequest("channelId", "videoId", "new comment");
            given(commentUseCase.updateComment(any(), any(), any()))
                .willThrow(new DomainNotFoundException("Comment Not Found."));

            // when
            mockMvc
                .perform(
                    put("/api/v1/comments/{commentId}", commentId)
                        .header(HeaderAttribute.X_AUTH_KEY, authKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                    status().isNotFound(),
                    jsonPath("$.type").value("notFound")
                );
        }

        @Test
        @DisplayName("404 Not Found, 존재하지 않는 댓글 수정 실패")
        void testGivenNoCommentWhenUpdateCommentThenNotFound() throws Exception {
            // given
            var commentId = "commentId";
            var request = new CommentRequest("channelId", "videoId", "new comment");
            given(commentUseCase.updateComment(any(), any(), any()))
                .willThrow(new ForbiddenRequestException("Request might not be properly authorized."));

            // when
            mockMvc
                .perform(
                    put("/api/v1/comments/{commentId}", commentId)
                        .header(HeaderAttribute.X_AUTH_KEY, authKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                    status().isForbidden(),
                    jsonPath("$.type").value("forbidden")
                );
        }
    }
}