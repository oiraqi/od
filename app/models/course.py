from odoo import models, fields


class Course(models.Model):
	_name = 'a3.course'
	_description = 'Course'

	name = fields.Char('Name', required=True)
	code = fields.Char('Code', required=True)
	instructor_id = fields.Many2one('a3.faculty', 'Instructor', required=True)
	sch = fields.Integer('SCH', required=True)
	code = fields.Char('Code', required=True)
	instructor_id = fields.Many2one('a3.faculty', 'Instructor', required=True)
	sch = fields.Integer('SCH', required=True)
	code = fields.Char('Code', required=True)
	instructor_id = fields.Many2one('a3.faculty', 'Instructor', required=True)
	sch = fields.Integer('SCH', required=True)
	code = fields.Char('Code', required=True)
	instructor_id = fields.Many2one('a3.faculty', 'Instructor', required=True)
	sch = fields.Integer('SCH', required=True)
	code = fields.Char('Code', required=True)
	instructor_id = fields.Many2one('a3.faculty', 'Instructor', required=True)
	sch = fields.Integer('SCH', required=True)
	