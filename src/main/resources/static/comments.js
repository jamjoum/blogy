var comments = [];

function findComment (commentId) {
  return comments[findCommentKey(commentId)];
}

function findCommentKey (commentId) {
  for (var key = 0; key < comments.length; key++) {
    if (comments[key].id == commentId) {
      return key;
    }
  }
}

var commentService = {
  findAll(fn) {
    axios
      .get('/post/1/comments')
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  create(comment, fn) {
    axios
      .post('/post/1/comment', comment)
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  update(id, comment, fn) {
    axios
      .put('/post/' + id +'/comments', comment)
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  deleteComment(id, fn) {
    axios
      .delete('/post/' + id +'/comments')
      .then(response => fn(response))
      .catch(error => console.log(error))
  }
}

var List = Vue.extend({
  template: '#comment-list',
  data: function () {
    return {comments: [], searchKey: ''};
  },
  computed: {
    filteredComments() {
      return this.comments.filter((comment) => {
      	return comment.author.indexOf(this.searchKey) > -1
      	  || comment.content.indexOf(this.searchKey) > -1
      })
    }
  },
  mounted() {
    commentService.findAll(r => {this.comments = r.data; comments = r.data})
  }
});

var CommentEdit = Vue.extend({
  template: '#comment-edit',
  data: function () {
    return {comment: findComment(this.$route.params.comment_id)};
  },
  methods: {
    updateComment: function () {
      commentService.update(this.comment.id, this.comment, r => router.push('/'))
    }
  }
});

var CommentDelete = Vue.extend({
  template: '#comment-delete',
  data: function () {
    return {comment: findComment(this.$route.params.comment_id)};
  },
  methods: {
    deleteComment: function () {
      commentService.deleteComment(this.comment.id, r => router.push('/'))
    }
  }
});

var AddComment = Vue.extend({
  template: '#add-comment',
  data() {
    return {
      comment: {name: '', description: ''}
    }
  },
  methods: {
    createComment() {
      commentService.create(this.comment, r => router.push('/'))
    }
  }
});

var router = new VueRouter({
	routes: [
		{path: '/', component: List},
		{path: '/add-comment', component: AddComment},
		{path: '/comment/:comment_id/edit', component: CommentEdit, name: 'comment-edit'},
		{path: '/comment/:comment_id/delete', component: CommentDelete, name: 'comment-delete'}
	]
});

new Vue({
  router
}).$mount('#app')
