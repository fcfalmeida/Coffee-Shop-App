const router = require('express').Router();

const user = require('./user');
const menu = require('./menu');
const debug = require('./debug');
const voucher = require('./voucher');
const order = require('./order');

router.use('/users', user);
router.use('/menu', menu);
router.use('/debug', debug);
router.use('/vouchers', voucher);
router.use('/orders', order);

module.exports = router;
