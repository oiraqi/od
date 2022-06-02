from odoo import models, fields


class Course(models.Model):
	_name = 'a3.course'
	_description = 'Course'

	name = fields.Char('Name', required=True)
	