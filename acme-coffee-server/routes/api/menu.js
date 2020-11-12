const router = require('express').Router();
const controller = require('../../controllers/menuController');
const authentication = require('../../middleware/authentication');

router.use('*', authentication);
router.get('/', controller.getMenu);

module.exports = router;