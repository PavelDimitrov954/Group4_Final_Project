//package com.example.group4_final_project;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class Helpers {
//    public static User createMockAdmin() {
//        User mockUser = createMockUser();
//        mockUser.setAdmin(true);
//        return mockUser;
//    }
//
//    public static User createMockUser() {
//        var mockUser = new User();
//        mockUser.setId(1);
//        mockUser.setUsername("MockUsername");
//        mockUser.setPassword("MockPassword");
//        mockUser.setLastName("MockLastName");
//        mockUser.setFirstName("MockFirstName");
//        mockUser.setEmail("mock@user.com");
//        return mockUser;
//    }
//
//    public static Post createMockPost() {
//        var mockPost = new Post();
//        mockPost.setId(1);
//        mockPost.setTitle("MockTitle");
//        mockPost.setContent("MockContent");
//        mockPost.setCreatedBy(createMockUser());
//
//
//        return mockPost;
//    }
//
//    public static Comment createMockComment() {
//        var mockComment = new Comment();
//        mockComment.setId(1);
//        mockComment.setContent("MockContent");
//        mockComment.setPost(createMockPost());
//        mockComment.setCreatedBy(createMockUser());
//        return mockComment;
//    }
//
//    public static FilterOptions createMockFilterOptions() {
//        return new FilterOptions(
//                "MockTitle",
//                "MockUsername",
//                "MockFirstName",
//                "desc",
//                "comedy"
//                );
//    }
//
//    public static FilterOptionsUser createMockFilterOptionsUser() {
//        return new FilterOptionsUser(
//                "MockTitle",
//                "mock@user.com",
//                "MockFirstName"
//               );
//    }
//
//    public static PostDto createPostDto() {
//        PostDto dto = new PostDto();
//        dto.setTitle("MockTitle");
//        dto.setContent("MockContentDto");
//        return dto;
//    }
//
//    public static CommentDto createCommentDto() {
//        CommentDto dto = new CommentDto();
//        dto.setContent("MockContentDto");
//        dto.setPostId(createMockPost().getId());
//        return dto;
//    }
//
//    public static UserDto createUserDto() {
//        UserDto dto = new UserDto();
//
//        dto.setUsername("MockUsername");
//        dto.setPassword("MockPassword");
//        dto.setLastName("MockLastName");
//        dto.setFirstName("MockFirstNameDto");
//        dto.setEmail("mock@user.com");
//        return dto;
//    }
//
//    public static Like createMockLike() {
//        var mockLike = new Like();
//       mockLike.setCreatedBy(createMockUser());
//       mockLike.setPost(createMockPost());
//       mockLike.setId(1);
//
//
//        return mockLike;
//    }
//
//
//    public static String toJson(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
